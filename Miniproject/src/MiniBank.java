import exception.BankException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Account;
import service.AccountTransaction;
import service.BankService;

public class MiniBank {
    private static final Path DATA_DIRECTORY = Path.of("data");

    public static void main(String[] args) {
        try (BankService bankService = new BankService(DATA_DIRECTORY);
                Scanner scanner = new Scanner(System.in)) {
            System.out.println("MiniBank ready. Loaded "
                    + bankService.listAccounts().size() + " account(s).");
            runMenu(bankService, scanner);
        } catch (IOException exception) {
            System.out.println("MiniBank could not load or save its data: "
                    + exception.getMessage());
        }
    }

    private static void runMenu(BankService bankService, Scanner scanner) {
        boolean running = true;
        while (running && scanner.hasNextLine()) {
            printMenu();
            String input = scanner.nextLine().trim();
            MenuOption option = parseOption(input);
            if (option == null) {
                System.out.println("Please choose a menu number from 1 to 9.");
                continue;
            }

            try {
                switch (option) {
                    case OPEN_ACCOUNT -> openAccount(bankService, scanner);
                    case DEPOSIT -> deposit(bankService, scanner);
                    case WITHDRAW -> withdraw(bankService, scanner);
                    case TRANSFER -> transfer(bankService, scanner);
                    case STATEMENT -> showStatement(bankService, scanner);
                    case LIST_ACCOUNTS -> listAccounts(bankService);
                    case BATCH -> runBatch(bankService, scanner);
                    case REPORT -> showReport(bankService);
                    case EXIT -> {
                        bankService.save();
                        System.out.println("Accounts saved. Goodbye!");
                        running = false;
                    }
                }
            } catch (BankException exception) {
                System.out.println("Banking operation failed: " + exception.getMessage());
            } catch (IOException exception) {
                System.out.println("Could not save transaction or report: "
                        + exception.getMessage());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.out.println("Operation interrupted. Returning to the menu.");
                running = false;
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid input: " + exception.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== MiniBank Menu =====");
        System.out.println("1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Account statement");
        System.out.println("6. List accounts");
        System.out.println("7. Process transaction batch");
        System.out.println("8. Generate and view end-of-day report");
        System.out.println("9. Save and exit");
        System.out.print("Choose an option: ");
    }

    private static MenuOption parseOption(String input) {
        return switch (input) {
            case "1" -> MenuOption.OPEN_ACCOUNT;
            case "2" -> MenuOption.DEPOSIT;
            case "3" -> MenuOption.WITHDRAW;
            case "4" -> MenuOption.TRANSFER;
            case "5" -> MenuOption.STATEMENT;
            case "6" -> MenuOption.LIST_ACCOUNTS;
            case "7" -> MenuOption.BATCH;
            case "8" -> MenuOption.REPORT;
            case "9" -> MenuOption.EXIT;
            default -> null;
        };
    }

    private static void openAccount(BankService service, Scanner scanner)
            throws BankException, IOException {
        System.out.print("Owner name (max 10 characters): ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Mobile (10 digits): ");
        String mobile = scanner.nextLine().trim();
        long openingBalance = readLong(scanner, "Opening balance: ");

        Account account = service.openAccount(name, email, mobile, openingBalance);
        System.out.println("Account opened: " + account.getAccountNumber());
    }

    private static void deposit(BankService service, Scanner scanner)
            throws BankException, IOException {
        String accountNumber = readAccountNumber(scanner);
        long amount = readLong(scanner, "Deposit amount: ");
        service.deposit(accountNumber, amount);
        System.out.println("Deposit successful.");
    }

    private static void withdraw(BankService service, Scanner scanner)
            throws BankException, IOException {
        String accountNumber = readAccountNumber(scanner);
        long amount = readLong(scanner, "Withdrawal amount: ");
        service.withdraw(accountNumber, amount);
        System.out.println("Withdrawal successful.");
    }

    private static void transfer(BankService service, Scanner scanner)
            throws BankException, IOException {
        System.out.print("From account number: ");
        String from = scanner.nextLine().trim();
        System.out.print("To account number: ");
        String to = scanner.nextLine().trim();
        long amount = readLong(scanner, "Transfer amount: ");
        service.transfer(from, to, amount);
        System.out.println("Transfer successful.");
    }

    private static void showStatement(BankService service, Scanner scanner)
            throws BankException {
        System.out.println(service.statement(readAccountNumber(scanner)));
    }

    private static void listAccounts(BankService service) {
        List<Account> accounts = service.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts are open.");
            return;
        }
        for (Account account : accounts) {
            System.out.println(account.getAccountNumber() + " | "
                    + account.getOwnerName() + " | balance: " + account.getBalance());
        }
    }

    private static void runBatch(BankService service, Scanner scanner)
            throws InterruptedException {
        long requestedCount = readLong(scanner, "Number of transactions: ");
        if (requestedCount < 0 || requestedCount > 1_000) {
            throw new IllegalArgumentException("Batch size must be between 0 and 1000.");
        }
        int count = (int) requestedCount;

        ArrayList<BankService.TransactionRequest> requests = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("Transaction " + (i + 1) + " (D=deposit, W=withdraw):");
            String typeInput = scanner.nextLine().trim().toUpperCase();
            AccountTransaction.Type type = switch (typeInput) {
                case "D" -> AccountTransaction.Type.DEPOSIT;
                case "W" -> AccountTransaction.Type.WITHDRAW;
                default -> throw new IllegalArgumentException("Choose D or W.");
            };
            String accountNumber = readAccountNumber(scanner);
            long amount = readLong(scanner, "Amount: ");
            requests.add(new BankService.TransactionRequest(accountNumber, type, amount));
        }

        for (String result : service.processBatch(requests)) {
            System.out.println(result);
        }
    }

    private static void showReport(BankService service) throws IOException {
        service.generateReport();
        for (String line : service.readReport()) {
            System.out.println(line);
        }
    }

    private static String readAccountNumber(Scanner scanner) {
        System.out.print("Account number: ");
        return scanner.nextLine().trim();
    }

    private static long readLong(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Please enter a whole number.", exception);
        }
    }
}
