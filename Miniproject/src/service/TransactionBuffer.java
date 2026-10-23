package service;

public class TransactionBuffer {
    private final Runnable[] transactions;
    private int head;
    private int tail;
    private int size;
    private boolean finished;

    public TransactionBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Buffer capacity must be greater than zero.");
        }
        transactions = new Runnable[capacity];
    }

    public synchronized void put(Runnable transaction) throws InterruptedException {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction must not be null.");
        }
        while (size == transactions.length && !finished) {
            wait();
        }
        if (finished) {
            throw new IllegalStateException("Cannot add a transaction after finishing.");
        }

        transactions[tail] = transaction;
        tail = (tail + 1) % transactions.length;
        size++;
        notifyAll();
    }

    public synchronized Runnable take() throws InterruptedException {
        while (size == 0 && !finished) {
            wait();
        }
        if (size == 0) {
            return null;
        }

        Runnable transaction = transactions[head];
        transactions[head] = null;
        head = (head + 1) % transactions.length;
        size--;
        notifyAll();
        return transaction;
    }

    public synchronized void finishAdding() {
        finished = true;
        notifyAll();
    }
}
