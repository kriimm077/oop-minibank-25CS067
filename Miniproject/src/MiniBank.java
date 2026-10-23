import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.BankInfo;
import model.TransactionType;

import service.Command;
import service.CommandParser;
import service.StatementFormatter;
import service.TransactionDemos;
import service.TransactionLog;
import service.ReportGenerator;
import service.AccountStore;
import persistence.StatePersister;

import util.WithdrawRule;
import util.Premium;
import util.AnnotationValidator;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import worker.AccountWorker;

public class MiniBank {

private static final int DEPOSIT_THREAD_COUNT = 10;
private static final int DEPOSITS_PER_THREAD = 10_000;
private static final long DEPOSIT_AMOUNT = 1;

private static class BankDemoResource implements AutoCloseable {
    @Override
    public void close() {
        System.out.println("Demo resource closed automatically.");
    }
}

private static class UnsafeDemoAccount extends SavingsAccount {
    private long unsafeBalance;

    private UnsafeDemoAccount() {
        super("Race Demo", 0, 0);
    }

    @Override
    public void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }

        long currentBalance = unsafeBalance;
        Thread.yield();
        unsafeBalance = currentBalance + amount;
    }

    @Override
    public long getBalance() {
        return unsafeBalance;
    }
}

public static void main(String[] args) {

    BankInfo bank = new BankInfo("MiniBank", "CHARUSAT");

    System.out.println("===== " + bank.name() + " =====");
    System.out.println("Branch: " + bank.branch());

    try {
        demonstrateConcurrentDeposits();
    } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
        System.out.println("Concurrent deposit demonstration was interrupted.");
        return;
    }

    try {
        TransactionDemos.runPoolDemo();
        TransactionDemos.runProducerConsumerDemo();
        TransactionDemos.runDeadlockAndOrderedTransferDemo();
    } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
        System.out.println("Transaction engine demonstration was interrupted.");
        return;
    }

    Account savings = new SavingsAccount(
            "Krima",
            5000,
            2000
    );

    Account current = new CurrentAccount(
            "Dhruv",
            3000,
            2000
    );

    Account fixedDeposit = new FixedDepositAccount(
            "Jagruti",
            10000
    );

    Account[] accounts = {
        savings,
        current,
        fixedDeposit
    };

    demonstrateAccountStore(accounts);

    try {
        demonstratePersistenceAndReporting(accounts);
    } catch (IOException exception) {
        System.out.println("Could not save or report bank data: " + exception.getMessage());
    }

    System.out.println("\n===== Account Interest Rates =====");

    for (Account account : accounts) {

        System.out.println(
                account.getOwnerName()
                + " | "
                + account.getClass().getSimpleName()
                + " | Interest Rate: "
                + account.interestRate()
                + "%"
        );
    }

    System.out.println("\n===== Exception Handling Tests =====");

    try (BankDemoResource resource = new BankDemoResource()) {
        try {
            savings.withdraw(3000);
            System.out.println("Savings withdrawal succeeded. Balance: "
                    + savings.getBalance());
        } catch (InvalidAmountException exception) {
            System.out.println("Invalid withdrawal: " + exception.getMessage());
        } catch (InsufficientFundsException exception) {
            System.out.println("Savings withdrawal failed: " + exception.getMessage());
            System.out.println("Shortfall: " + exception.getShortfall());
        } finally {
            System.out.println("Savings withdrawal attempt finished.");
        }

        try {
            current.withdraw(4000);
            System.out.println("Current account withdrawal succeeded. Balance: "
                    + current.getBalance());
        } catch (InvalidAmountException exception) {
            System.out.println("Invalid withdrawal: " + exception.getMessage());
        } catch (InsufficientFundsException exception) {
            System.out.println("Current account withdrawal failed: " + exception.getMessage());
            System.out.println("Shortfall: " + exception.getShortfall());
        }

        try {
            fixedDeposit.withdraw(1000);
        } catch (InvalidAmountException exception) {
            System.out.println("Invalid withdrawal: " + exception.getMessage());
        } catch (InsufficientFundsException exception) {
            System.out.println("Fixed deposit withdrawal failed: " + exception.getMessage());
            System.out.println("Shortfall: " + exception.getShortfall());
        }

        try {
            savings.deposit(0);
        } catch (InvalidAmountException exception) {
            System.out.println("Deposit failed: " + exception.getMessage());
        }

        try {
            current.transfer(savings, 500);
            System.out.println("Transfer succeeded. Current balance: "
                    + current.getBalance() + ", savings balance: " + savings.getBalance());
        } catch (BankException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        }

        try {
            savings.transfer(null, 100);
        } catch (AccountNotFoundException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        } catch (BankException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        }
    } finally {
        System.out.println("Banking operation examples finished.");
    }

    System.out.println("\n===== instanceof Test =====");

    for (Account account : accounts) {

        if (account instanceof SavingsAccount savingsAccount) {

            System.out.println(
                    "Savings Account found."
            );

            System.out.println(
                    "Minimum balance: "
                    + savingsAccount.getMinBalance()
            );
        }
    }
System.out.println("\n===== Premium Test =====");

if (fixedDeposit instanceof Premium) {
    System.out.println("Fixed Deposit is a Premium account.");
}
    System.out.println("\n===== WithdrawRule Tests =====");

    WithdrawRule anonymousRule = new WithdrawRule() {
        @Override
        public boolean allow(Account account, long amount) {
            return amount > 0 && amount <= account.getBalance();
        }
    };

    System.out.println(
            "Anonymous rule: "
            + anonymousRule.allow(savings, 1000)
    );

    WithdrawRule lambdaRule =
            (account, amount) -> amount > 0 && amount <= account.getBalance();

    System.out.println(
            "Lambda rule: "
            + lambdaRule.allow(savings, 1000)
    );
    System.out.println("\n===== Annotation Validation Tests =====");

    Account badAccount = new SavingsAccount("Riya", -100, 2000);
    Account longName = new SavingsAccount("Bartholomew Jr", 5000, 2000);
    Account goodAccount = new SavingsAccount("Aman", 5000, 2000);

    for (String error : AnnotationValidator.validate(badAccount)) {
        System.out.println("Bad account error: " + error);
    }

    for (String error : AnnotationValidator.validateWithFieldNames(longName)) {
        System.out.println("Long name error: " + error);
    }

    System.out.println(
            "Good account errors: "
            + AnnotationValidator.validate(goodAccount).length
    );
    Scanner sc = new Scanner(System.in);

    while (true) {

        System.out.println("\n===== MiniBank Menu =====");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();

        MenuOption option = switch (choice) {

            case 1 -> MenuOption.OPEN_ACCOUNT;
            case 2 -> MenuOption.DEPOSIT;
            case 3 -> MenuOption.WITHDRAW;
            case 4 -> MenuOption.TRANSFER;
            case 5 -> MenuOption.EXIT;

            default -> null;
        };

        if (option == null) {

            System.out.println(
                    "Invalid choice. Please try again."
            );

            continue;
        }

        String message = switch (option) {

            case OPEN_ACCOUNT ->
                "Open Account — to be implemented in a later lab";

            case DEPOSIT ->
                "Deposit — to be implemented in a later lab";

            case WITHDRAW ->
                "Withdraw — to be implemented in a later lab";

            case TRANSFER ->
                "Transfer — to be implemented in a later lab";

            case EXIT ->
                "Goodbye!";
        };

        System.out.println(message);

        if (option == MenuOption.EXIT) {
            break;
        }
    }

    sc.close();
}

private static void demonstrateConcurrentDeposits() throws InterruptedException {
    long expectedBalance = (long) DEPOSIT_THREAD_COUNT
            * DEPOSITS_PER_THREAD
            * DEPOSIT_AMOUNT;

    System.out.println("\n===== Concurrent Deposit Test =====");
    Account unsafeAccount = new UnsafeDemoAccount();
    runDepositWorkers(unsafeAccount);
    System.out.println("Without synchronization: " + unsafeAccount.getBalance());
    System.out.println("Expected balance: " + expectedBalance);

    Account safeAccount = new SavingsAccount("Thread Safe", 0, 0);
    runDepositWorkers(safeAccount);
    System.out.println("With synchronized deposit: " + safeAccount.getBalance());
    System.out.println("Expected balance: " + expectedBalance);
}

private static void runDepositWorkers(Account account) throws InterruptedException {
    Thread[] threads = new Thread[DEPOSIT_THREAD_COUNT];

    for (int i = 0; i < threads.length; i++) {
        threads[i] = new Thread(
                new AccountWorker(account, DEPOSITS_PER_THREAD, DEPOSIT_AMOUNT),
                "Depositor-" + (i + 1));
        threads[i].start();
    }

    for (Thread thread : threads) {
        thread.join();
    }
}

private static void demonstratePersistenceAndReporting(Account[] accounts) throws IOException {
    Path dataFolder = Path.of("data");
    Path accountsFile = dataFolder.resolve("accounts.dat");
    Path logsFolder = dataFolder.resolve("logs");
    Path transactionFile = logsFolder.resolve("transactions.log");
    Path reportFile = dataFolder.resolve("end-of-day-report.txt");

    try {
        accounts[0].deposit(250);
        TransactionLog.append(transactionFile,
                "DEPOSIT 250 " + accounts[0].getAccountNumber());

        accounts[1].withdraw(100);
        TransactionLog.append(transactionFile,
                "WITHDRAW 100 " + accounts[1].getAccountNumber());
    } catch (BankException exception) {
        throw new IOException("Could not record the sample banking transactions.", exception);
    }

    StatePersister.save(accounts, accountsFile);
    Account[] loadedAccounts = StatePersister.load(accountsFile);

    System.out.println("\n===== Reloaded Accounts =====");
    for (Account account : loadedAccounts) {
        System.out.println(account);
    }

    ReportGenerator.generate(logsFolder, reportFile);
    System.out.println("\n===== End-of-Day Report =====");
    for (String line : Files.readAllLines(reportFile)) {
        System.out.println(line);
    }
}

private static void demonstrateAccountStore(Account[] accounts) {
    AccountStore accountStore = new AccountStore();
    for (Account account : accounts) {
        accountStore.add(account);
        accountStore.addTransaction("Account opened: " + account.getAccountNumber());
    }

    System.out.println("\n===== Accounts Sorted by Balance =====");
    for (Account account : accountStore.sortedByBalance()) {
        System.out.println(account.getAccountNumber() + " | " + account.getOwnerName()
                + " | balance: " + account.getBalance());
    }
    System.out.println("Stored accounts: " + accountStore.getAll().size());
    System.out.println("Transaction history entries: "
            + accountStore.getTransactionHistory().size());
}


}
