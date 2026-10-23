package service;

import exception.BankException;
import model.Account;

public final class OrderedTransfer {
    private OrderedTransfer() {
    }

    public static void transfer(Account from, Account to, long amount) throws BankException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Both transfer accounts are required.");
        }
        if (from == to) {
            throw new IllegalArgumentException("Source and destination must be different accounts.");
        }

        Account firstLock;
        Account secondLock;
        if (from.getAccountNumber().compareTo(to.getAccountNumber()) < 0) {
            firstLock = from;
            secondLock = to;
        } else {
            firstLock = to;
            secondLock = from;
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
