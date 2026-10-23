package service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Account;

public class TransactionProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private boolean stopped;

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null.");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public synchronized void submit(Runnable task) {
        if (stopped) {
            throw new IllegalStateException("Transaction processor has already stopped.");
        }
        executor.execute(task);
    }

    public void stop() throws InterruptedException {
        synchronized (this) {
            if (stopped) {
                return;
            }
            stopped = true;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Transaction tasks did not terminate.");
                }
            }
        } catch (InterruptedException exception) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            throw exception;
        }
    }
}
