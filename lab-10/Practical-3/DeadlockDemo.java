import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DeadlockDemo {

    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();
    private static final Object SAFE_LOCK_A = new Object();
    private static final Object SAFE_LOCK_B = new Object();

    public static void main(String[] args) throws InterruptedException {
        demonstrateDeadlock();
        demonstrateConsistentLockOrder();
    }

    private static void demonstrateDeadlock() throws InterruptedException {
        CountDownLatch bothThreadsHaveFirstLock = new CountDownLatch(2);

        Thread firstThread = new Thread(
                () -> lockInOppositeOrder(LOCK_A, LOCK_B, bothThreadsHaveFirstLock),
                "Thread-1");
        Thread secondThread = new Thread(
                () -> lockInOppositeOrder(LOCK_B, LOCK_A, bothThreadsHaveFirstLock),
                "Thread-2");

        // Daemon threads let the demonstration finish even though these threads stay deadlocked
        firstThread.setDaemon(true);
        secondThread.setDaemon(true);
        firstThread.start();
        secondThread.start();

        if (bothThreadsHaveFirstLock.await(2, TimeUnit.SECONDS)) {
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            while ((firstThread.getState() != Thread.State.BLOCKED
                    || secondThread.getState() != Thread.State.BLOCKED)
                    && System.nanoTime() < deadline) {
                Thread.sleep(10);
            }

            if (firstThread.getState() == Thread.State.BLOCKED
                    && secondThread.getState() == Thread.State.BLOCKED) {
                System.out.println("Deadlock reproduced: each thread holds one lock "
                        + "and is waiting for the other.");
            } else {
                System.out.println("Both threads got their first lock, but the deadlock "
                        + "could not be confirmed.");
            }
        } else {
            System.out.println("Could not start both deadlock demonstration threads.");
        }
    }

    private static void lockInOppositeOrder(
            Object firstLock, Object secondLock, CountDownLatch bothThreadsHaveFirstLock) {
        synchronized (firstLock) {
            bothThreadsHaveFirstLock.countDown();
            try {
                bothThreadsHaveFirstLock.await();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized (secondLock) {
                System.out.println(Thread.currentThread().getName() + " acquired both locks.");
            }
        }
    }

    private static void demonstrateConsistentLockOrder() throws InterruptedException {
        Thread firstThread = new Thread(DeadlockDemo::lockInConsistentOrder, "Safe-Thread-1");
        Thread secondThread = new Thread(DeadlockDemo::lockInConsistentOrder, "Safe-Thread-2");

        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();

        System.out.println("Both safe threads finished using the same lock order.");
    }

    private static void lockInConsistentOrder() {
        synchronized (SAFE_LOCK_A) {
            synchronized (SAFE_LOCK_B) {
                System.out.println(Thread.currentThread().getName() + " acquired both locks.");
            }
        }
    }
}
