package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private boolean stopped;

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
