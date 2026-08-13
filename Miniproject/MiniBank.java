import java.util.Scanner;

record BankInfo(String name, String branch) {
}

public class MiniBank {

    public static void main(String[] args) {

        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT");

        System.out.println("===== " + bank.name() + " =====");
        System.out.println("Branch: " + bank.branch());

      

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

       

        System.out.println("\n===== Withdrawal Tests =====");

        System.out.println(
                "Savings withdraw 3000: "
                + savings.withdraw(3000)
        );

        System.out.println(
                "Savings balance: "
                + savings.getBalance()
        );

        System.out.println(
                "Current withdraw 4000: "
                + current.withdraw(4000)
        );

        System.out.println(
                "Current balance: "
                + current.getBalance()
        );

        System.out.println(
                "Fixed Deposit withdraw 1000: "
                + fixedDeposit.withdraw(1000)
        );

        System.out.println(
                "Fixed Deposit balance: "
                + fixedDeposit.getBalance()
        );

       
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
}