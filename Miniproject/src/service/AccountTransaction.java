package service;

import exception.BankException;
import model.Account;

public class AccountTransaction implements Runnable {
    public enum Type {
        DEPOSIT,
        WITHDRAW
    }

    private final String transactionId;
    private final Account account;
    private final Type type;
    private final long amount;

    public AccountTransaction(String transactionId, Account account, Type type, long amount) {
        this.transactionId = transactionId;
        this.account = account;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            if (type == Type.DEPOSIT) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }
            System.out.println(transactionId + " " + type + " completed on " + threadName);
        } catch (BankException exception) {
            System.out.println(transactionId + " failed on " + threadName
                    + ": " + exception.getMessage());
        }
    }
}
