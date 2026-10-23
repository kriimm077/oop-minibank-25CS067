public class CounterRace {

    private static final int THREAD_COUNT = 8;
    private static final int INCREMENTS_PER_THREAD = 100_000;

    private static class Counter {
        private int value = 0;

        // This version is intentionally not synchronized to demonstrate a race condition
        public void incrementWithoutSynchronization() {
            value++;
        }

        public synchronized void incrementWithSynchronization() {
            value++;
        }

        public int getValue() {
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int expectedTotal = THREAD_COUNT * INCREMENTS_PER_THREAD;

        Counter unsafeCounter = new Counter();
        runThreads(unsafeCounter, false);
        System.out.println("Without synchronization: " + unsafeCounter.getValue());
        System.out.println("Expected total: " + expectedTotal);

        Counter safeCounter = new Counter();
        runThreads(safeCounter, true);
        System.out.println();
        System.out.println("With synchronized increment: " + safeCounter.getValue());
        System.out.println("Expected total: " + expectedTotal);

        if (safeCounter.getValue() == expectedTotal) {
            System.out.println("Synchronized result is correct.");
        } else {
            System.out.println("Synchronized result is incorrect.");
        }
    }

    private static void runThreads(Counter counter, boolean useSynchronization)
            throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    if (useSynchronization) {
                        counter.incrementWithSynchronization();
                    } else {
                        counter.incrementWithoutSynchronization();
                    }
                }
            });
            threads[i].start();
        }

        // Wait for every worker to finish before reading the counter
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
