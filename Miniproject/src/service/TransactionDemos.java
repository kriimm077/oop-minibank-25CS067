package service;

import exception.BankException;
import model.Account;
import model.CurrentAccount;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class TransactionDemos {
    private static final int BUFFER_CAPACITY = 3;
    private static final int BUFFERED_TRANSACTION_COUNT = 10;

    private TransactionDemos() {
    }

    public static void runPoolDemo() throws InterruptedException {
        System.out.println("\n===== Managed Transaction Pool =====");
        Account account = new CurrentAccount("Pool Demo", 1_000, 0);
        TransactionProcessor processor = new TransactionProcessor();
        processor.addAccount(account);

        try {
            for (int i = 1; i <= 12; i++) {
                AccountTransaction.Type type = i % 2 == 0
                        ? AccountTransaction.Type.WITHDRAW
                        : AccountTransaction.Type.DEPOSIT;
                Account sharedAccount = processor.getAccount(account.getAccountNumber());
                processor.submit(new AccountTransaction(
                        "Pool-" + i, sharedAccount, type, 10));
            }
        } finally {
            processor.stop();
        }

        System.out.println("Pool finished. Account balance: " + account.getBalance());
    }

    public static void runProducerConsumerDemo() throws InterruptedException {
        System.out.println("\n===== Producer-Consumer Transaction Buffer =====");
        TransactionBuffer buffer = new TransactionBuffer(BUFFER_CAPACITY);
        Account account = new CurrentAccount("Buffer Demo", 1_000, 0);

        Thread consumer = new Thread(() -> {
            try {
                Runnable transaction;
                while ((transaction = buffer.take()) != null) {
                    transaction.run();
                }
                System.out.println("Consumer finished processing transactions.");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer was interrupted.");
            }
        }, "Transaction-Consumer");

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= BUFFERED_TRANSACTION_COUNT; i++) {
                    AccountTransaction.Type type = i % 2 == 0
                            ? AccountTransaction.Type.WITHDRAW
                            : AccountTransaction.Type.DEPOSIT;
                    buffer.put(new AccountTransaction(
                            "Buffered-" + i, account, type, 5));
                    System.out.println("Produced Buffered-" + i);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.out.println("Producer was interrupted.");
            } finally {
                buffer.finishAdding();
            }
        }, "Transaction-Producer");

        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        System.out.println("Buffer demo finished. Account balance: " + account.getBalance());
    }

    public static void runDeadlockAndOrderedTransferDemo() throws InterruptedException {
        System.out.println("\n===== Transfer Lock Ordering =====");
        Account accountA = new CurrentAccount("Deadlock A", 1_000, 0);
        Account accountB = new CurrentAccount("Deadlock B", 1_000, 0);
        demonstrateOppositeOrderDeadlock(accountA, accountB);

        Account safeA = new CurrentAccount("Safe A", 1_000, 0);
        Account safeB = new CurrentAccount("Safe B", 1_000, 0);
        Thread firstTransfer = new Thread(
                () -> runOrderedTransfer(safeA, safeB), "Safe-Transfer-A-to-B");
        Thread secondTransfer = new Thread(
                () -> runOrderedTransfer(safeB, safeA), "Safe-Transfer-B-to-A");

        firstTransfer.start();
        secondTransfer.start();
        firstTransfer.join();
        secondTransfer.join();

        System.out.println("Ordered transfers finished. Balances: "
                + safeA.getBalance() + " and " + safeB.getBalance());
    }

    private static void demonstrateOppositeOrderDeadlock(Account accountA, Account accountB)
            throws InterruptedException {
        CountDownLatch bothThreadsHoldFirstLock = new CountDownLatch(2);
        Thread transferAtoB = new Thread(
                () -> transferWithOppositeLocks(
                        accountA, accountB, bothThreadsHoldFirstLock),
                "Unsafe-Transfer-A-to-B");
        Thread transferBtoA = new Thread(
                () -> transferWithOppositeLocks(
                        accountB, accountA, bothThreadsHoldFirstLock),
                "Unsafe-Transfer-B-to-A");

        transferAtoB.setDaemon(true);
        transferBtoA.setDaemon(true);
        transferAtoB.start();
        transferBtoA.start();

        if (!bothThreadsHoldFirstLock.await(2, TimeUnit.SECONDS)) {
            System.out.println("Could not start both unsafe transfer threads.");
            return;
        }

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
        while ((transferAtoB.getState() != Thread.State.BLOCKED
                || transferBtoA.getState() != Thread.State.BLOCKED)
                && System.nanoTime() < deadline) {
            Thread.sleep(10);
        }

        if (transferAtoB.getState() == Thread.State.BLOCKED
                && transferBtoA.getState() == Thread.State.BLOCKED) {
            System.out.println("Deadlock reproduced: transfers wait on opposite account locks.");
        } else {
            System.out.println("Opposite-order transfers did not reach a confirmed deadlock.");
        }
    }

    private static void transferWithOppositeLocks(
            Account from, Account to, CountDownLatch bothThreadsHoldFirstLock) {
        synchronized (from) {
            bothThreadsHoldFirstLock.countDown();
            try {
                bothThreadsHoldFirstLock.await();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized (to) {
                performTransfer(from, to);
            }
        }
    }

    private static void runOrderedTransfer(Account from, Account to) {
        try {
            OrderedTransfer.transfer(from, to, 100);
            System.out.println(Thread.currentThread().getName() + " completed.");
        } catch (BankException exception) {
            System.out.println(Thread.currentThread().getName()
                    + " failed: " + exception.getMessage());
        }
    }

    private static void performTransfer(Account from, Account to) {
        try {
            from.withdraw(100);
            to.deposit(100);
        } catch (BankException exception) {
            System.out.println(Thread.currentThread().getName()
                    + " failed: " + exception.getMessage());
        }
    }
}
