import java.util.*;


record BankInfo(String name, String branch) {
}

public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT");
        System.out.println(bank);
        Scanner sc = new Scanner(System.in);

while (true) {

    System.out.println("\n===== MiniBank =====");
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
   default -> {
    System.out.println("Invalid choice");
    yield null;
}
};

 
if (option == null) {
    continue;
}
    switch (option) {
        case OPEN_ACCOUNT -> System.out.println("Opening a new account...");
        case DEPOSIT -> System.out.println("Depositing money...");
        case WITHDRAW -> System.out.println("Withdrawing money...");
        case TRANSFER -> System.out.println("Transferring money...");
        case EXIT -> {
            System.out.println("Exiting the application. Goodbye!");
            return;
        }
        default -> System.out.println("Invalid choice. Please try again.");
    }
}
}
}