public class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public void deposit(long amount) {
        if (amount < 0) {
            return;
        }

        balance += amount;
    }

    public boolean withdraw(long amount) {
        if (amount < 0) {
            return false;
        }

        if (amount <= balance) {
            balance -= amount;
            return true;
        }

        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public static void main(String[] args) {

        Account[] accounts = {
            new Account("Krima", 5000),
            new Account("Dhruv", 3000),
            new Account("Jagruti", 1000)
        };

        accounts[0].deposit(2000);

        System.out.println("After deposit: " + accounts[0].getBalance());

        boolean success = accounts[0].withdraw(3000);
        System.out.println("Withdraw 3000: " + success);
        System.out.println("Balance: " + accounts[0].getBalance());

        success = accounts[0].withdraw(10000);
        System.out.println("Withdraw 10000: " + success);
        System.out.println("Balance: " + accounts[0].getBalance());

        accounts[1].deposit(1000);
        accounts[2].deposit(500);

        System.out.println("\nAll account balances:");

        for (Account account : accounts) {
            System.out.println(
                account.getAccountNumber()
                + " - "
                + account.getOwnerName()
                + " - Balance: "
                + account.getBalance()
            );
        }
    }
}