import java.util.Scanner;

record BankInfo(String name, String branch) {
}

public class MiniBank {

    public static void main(String[] args) {

        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT");

        System.out.println("===== " + bank.name() + " =====");
        System.out.println("Branch: " + bank.branch());

        // ==============================
        // Practical 3 - Account testing
        // ==============================

        Account account1 = new Account("Krima", 5000);
        Account account2 = new Account("Dhruv", 3000);

        // toString() test
        System.out.println("\n===== Account Details =====");
        System.out.println(account1);
        System.out.println(account2);

        // equals() test
        System.out.println("\n===== Equals Test =====");
        System.out.println("account1 equals account2: "
                + account1.equals(account2));

        // instanceof test
        System.out.println("\n===== instanceof Test =====");

        Object obj = account1;

        if (obj instanceof Account) {
            System.out.println("obj is an Account");
        }

        // ==============================
        // Practical 3 - Customer testing
        // ==============================

        Customer customer = new Customer(
                "Krima",
                "krima@example.com",
                "9876543210"
        );

        Customer.Address address = new Customer.Address(
                "123 Main Road",
                "Anand",
                "388001"
        );

        customer.setAddress(address);

        System.out.println("\n===== Customer Address =====");
        System.out.println("Line: " + customer.getAddress().getLine());
        System.out.println("City: " + customer.getAddress().getCity());
        System.out.println("Pincode: " + customer.getAddress().getPincode());

        // clone() test
        Customer clonedCustomer = customer.clone();

        System.out.println("\n===== Clone Test =====");
        System.out.println("Original Customer: " + customer.getName());
        System.out.println("Cloned Customer: " + clonedCustomer.getName());
        System.out.println("Cloned Customer ID: "
                + clonedCustomer.getCustomerId());

        // ==============================
        // Lab 04 - Validator testing
        // ==============================

        System.out.println("\n===== Validator Tests =====");

        // Mobile
        System.out.println("Valid mobile: "
                + Validator.isValidMobile("9876543210"));

        System.out.println("Invalid mobile: "
                + Validator.isValidMobile("1234567890"));

        // Email
        System.out.println("Valid email: "
                + Validator.isValidEmail("abc@example.com"));

        System.out.println("Invalid email: "
                + Validator.isValidEmail("abc@example"));

        // PAN
        System.out.println("Valid PAN: "
                + Validator.isValidPan("ABCDE1234F"));

        System.out.println("Invalid PAN: "
                + Validator.isValidPan("ABC123"));

        // IFSC
        System.out.println("Valid IFSC: "
                + Validator.isValidIfsc("SBIN0001234"));

        System.out.println("Invalid IFSC: "
                + Validator.isValidIfsc("SBIN123"));

        // ==============================
        // Lab 04 - Command Parser testing
        // ==============================

        System.out.println("\n===== Command Parser Test =====");

        Command command = CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("Type: " + command.type());
        System.out.println("Account Number: " + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        // ==============================
        // Lab 04 - Statement Formatter
        // ==============================

        System.out.println("\n===== Account Statement =====");

        System.out.println(
                StatementFormatter.buildStatement(account1)
        );

        // ==============================
        // MiniBank menu
        // ==============================

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
                System.out.println("Invalid choice. Please try again.");
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
}