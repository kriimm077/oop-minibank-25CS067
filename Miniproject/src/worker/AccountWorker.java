package worker;

import exception.InvalidAmountException;
import model.Account;

public class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public AccountWorker(Account account, int times, long amount) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null.");
        }
        if (times < 0) {
            throw new IllegalArgumentException("Times must not be negative.");
        }
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " started.");

        try {
            for (int i = 0; i < times; i++) {
                account.deposit(amount);
            }
        } catch (InvalidAmountException exception) {
            System.out.println(threadName + " could not deposit: " + exception.getMessage());
        } finally {
            System.out.println(threadName + " finished.");
        }
    }
}
