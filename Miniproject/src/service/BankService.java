package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentLinkedQueue;
import model.Account;
import model.SavingsAccount;
import persistence.StatePersister;
import util.AnnotationValidator;
import util.Validator;

public class BankService implements AutoCloseable {
    public record TransactionRequest(
            String accountNumber,
            AccountTransaction.Type type,
            long amount) {
    }

    private final Repository<Account> accounts = new Repository<>();
    private final TransactionProcessor transactionProcessor = new TransactionProcessor();
    private final Path accountsFile;
    private final Path logsDirectory;
    private final Path reportFile;
    private final Object logLock = new Object();

    public BankService(Path dataDirectory) throws IOException {
        accountsFile = dataDirectory.resolve("accounts.dat");
        logsDirectory = dataDirectory.resolve("logs");
        reportFile = dataDirectory.resolve("end-of-day-report.txt");

        if (Files.exists(accountsFile)) {
            for (Account account : StatePersister.load(accountsFile)) {
                accounts.save(account);
                transactionProcessor.addAccount(account);
            }
        }
    }

    public Account openAccount(String ownerName, String email, String mobile, long openingBalance)
            throws BankException, IOException {
        if (ownerName == null || ownerName.isBlank()) {
            throw new InvalidAmountException("Account owner name must not be blank.");
        }
        if (!Validator.isValidEmail(email)) {
            throw new InvalidAmountException("Please enter a valid email address.");
        }
        if (!Validator.isValidMobile(mobile)) {
            throw new InvalidAmountException("Please enter a valid 10-digit mobile number.");
        }

        Account account = new SavingsAccount(ownerName, openingBalance, 0);
        String[] validationErrors = AnnotationValidator.validate(account);
        if (validationErrors.length > 0) {
            throw new InvalidAmountException(String.join("; ", validationErrors));
        }

        accounts.save(account);
        transactionProcessor.addAccount(account);
        appendLog("DEPOSIT " + openingBalance + " " + account.getAccountNumber());
        return account;
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("No account found with number " + accountNumber + ".");
        }
        return account;
    }

    public void deposit(String accountNumber, long amount) throws BankException, IOException {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
        appendLog("DEPOSIT " + amount + " " + accountNumber);
    }

    public void withdraw(String accountNumber, long amount) throws BankException, IOException {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
        appendLog("WITHDRAW " + amount + " " + accountNumber);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, long amount)
            throws BankException, IOException {
        Account from = getAccount(fromAccountNumber);
        Account to = getAccount(toAccountNumber);
        OrderedTransfer.transfer(from, to, amount);
        appendLog("WITHDRAW " + amount + " " + fromAccountNumber);
        appendLog("DEPOSIT " + amount + " " + toAccountNumber);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(getAccount(accountNumber));
    }

    public ArrayList<Account> listAccounts() {
        ArrayList<Account> allAccounts = accounts.findAll();
        allAccounts.sort(Comparator.comparing(Account::getAccountNumber));
        return allAccounts;
    }

    public List<String> processBatch(List<TransactionRequest> requests)
            throws InterruptedException {
        Objects.requireNonNull(requests, "Transaction requests must not be null.");
        if (requests.isEmpty()) {
            return List.of();
        }
        for (TransactionRequest request : requests) {
            Objects.requireNonNull(request, "Transaction request must not be null.");
            Objects.requireNonNull(request.type(), "Transaction type must not be null.");
            if (request.accountNumber() == null || request.accountNumber().isBlank()) {
                throw new IllegalArgumentException("Transaction account number must not be blank.");
            }
        }

        CountDownLatch completed = new CountDownLatch(requests.size());
        ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();

        for (TransactionRequest request : requests) {
            transactionProcessor.submit(() -> {
                try {
                    Account account = transactionProcessor.getAccount(request.accountNumber());
                    if (account == null) {
                        throw new AccountNotFoundException(
                                "No account found with number " + request.accountNumber() + ".");
                    }

                    if (request.type() == AccountTransaction.Type.DEPOSIT) {
                        account.deposit(request.amount());
                    } else {
                        account.withdraw(request.amount());
                    }
                    try {
                        appendLog(request.type().name() + " " + request.amount()
                                + " " + request.accountNumber());
                        results.add(request.type() + " " + request.amount()
                                + " to " + request.accountNumber() + " succeeded.");
                    } catch (IOException exception) {
                        results.add("Transaction was applied but could not be logged: "
                                + exception.getMessage());
                    }
                } catch (BankException exception) {
                    results.add(request.type() + " " + request.amount()
                            + " to " + request.accountNumber() + " failed: "
                            + exception.getMessage());
                } finally {
                    completed.countDown();
                }
            });
        }

        try {
            completed.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw exception;
        }
        return List.copyOf(results);
    }

    public void generateReport() throws IOException {
        ReportGenerator.generate(logsDirectory, reportFile);
    }

    public List<String> readReport() throws IOException {
        return Files.readAllLines(reportFile);
    }

    public void save() throws IOException {
        StatePersister.save(accounts.findAll().toArray(Account[]::new), accountsFile);
    }

    private void appendLog(String line) throws IOException {
        synchronized (logLock) {
            TransactionLog.append(logsDirectory.resolve("transactions.log"), line);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            transactionProcessor.stop();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while stopping the transaction processor.", exception);
        }
        save();
    }
}
