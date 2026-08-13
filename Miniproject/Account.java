import java.util.Objects;

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

    // Practical 3: toString()
  
    public String toString() {
        return "Account{"
                + "accountNumber='" + accountNumber + '\''
                + ", ownerName='" + ownerName + '\''
                + ", balance=" + balance
                + ", active=" + active
                + '}';
    }

    // Practical 3: equals()
    // Two accounts are equal when their account numbers are equal.
   
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account other = (Account) o;

        return accountNumber.equals(other.accountNumber);
    }

    // Practical 3: hashCode()

    public int hashCode() {
        return Objects.hash(accountNumber);
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
            System.out.println(account);
        }

        // Practical 3: equals() test
        System.out.println("\nEquality test:");
        System.out.println("Account 1 equals Account 2: "
                + accounts[0].equals(accounts[1]));

        // Practical 3: instanceof test
        Object obj = accounts[0];

        System.out.println("\ninstanceof test:");
        if (obj instanceof Account) {
            System.out.println("obj is an Account");
        }
    }
}