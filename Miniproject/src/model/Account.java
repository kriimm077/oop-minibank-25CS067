package model;

import java.util.Objects;
import java.io.Serializable;
import util.Transactable;
import util.InterestBearing;
import model.annotation.Id;
import model.annotation.Positive;
import model.annotation.MaxLength;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public abstract class Account implements Transactable, InterestBearing, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private final String accountNumber;

    @MaxLength(10)
    private String ownerName;

    @Positive
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

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }

        balance += amount;
    }

    @Override
    public synchronized void withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }

        if (!canWithdraw(amount)) {
            long available = Math.max(0, availableToWithdraw());
            throw new InsufficientFundsException(amount - available);
        }

        balance -= amount;
    }

    public void transfer(Account to, long amount) throws BankException {
        try {
            if (to == null) {
                throw new AccountNotFoundException("The destination account was not found.");
            }

            withdraw(amount);
            to.deposit(amount);
        } catch (BankException exception) {
            throw exception;
        } finally {
            System.out.println("Transfer attempt completed.");
        }
    }

    @Override
    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    protected long availableToWithdraw() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public synchronized String toString() {
        return "Account{"
                + "accountNumber='" + accountNumber + '\''
                + ", ownerName='" + ownerName + '\''
                + ", balance=" + balance
                + ", active=" + active
                + '}';
    }

    @Override
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

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}