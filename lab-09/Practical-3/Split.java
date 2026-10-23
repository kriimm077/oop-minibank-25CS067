import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class Split {

    private static final int ARRAY_SIZE = 1_000_000;
    private static final int THREAD_COUNT = 8;

    private static class SumResult {
        private final long total;
        private final long elapsedNanos;

        public SumResult(long total, long elapsedNanos) {
            this.total = total;
            this.elapsedNanos = elapsedNanos;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] numbers = new int[ARRAY_SIZE];
        long expectedTotal = 0;

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
            expectedTotal += numbers[i];
        }

        SumResult unsafeResult = sumWithSharedTotal(numbers, "unsafe");
        SumResult synchronizedResult = sumWithSharedTotal(numbers, "synchronized");
        SumResult atomicResult = sumWithSharedTotal(numbers, "atomic");

        System.out.println("Expected total: " + expectedTotal);
        printResult("Without synchronization", unsafeResult, expectedTotal);
        printResult("With synchronized updates", synchronizedResult, expectedTotal);
        printResult("With AtomicLong", atomicResult, expectedTotal);
        System.out.println();
        System.out.println("Time measures thread startup and summing; lower time is faster.");
        System.out.println("Timing can vary between runs, so correctness matters too.");
    }

    private static SumResult sumWithSharedTotal(int[] numbers, String method)
            throws InterruptedException {
        long startTime = System.nanoTime();
        long[] sharedTotal = {0};
        Object lock = new Object();
        AtomicLong atomicTotal = new AtomicLong(0);

        // In the unsafe example, every worker reads the same old total before updating it.
        CountDownLatch allWorkersReadTotal = new CountDownLatch(THREAD_COUNT);
        Thread[] workers = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int start = i * numbers.length / THREAD_COUNT;
            final int end = (i + 1) * numbers.length / THREAD_COUNT;

            workers[i] = new Thread(() -> {
                long partialTotal = 0;
                for (int j = start; j < end; j++) {
                    partialTotal += numbers[j];
                }

                if ("unsafe".equals(method)) {
                    long oldTotal = sharedTotal[0];
                    allWorkersReadTotal.countDown();
                    try {
                        allWorkersReadTotal.await();
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    sharedTotal[0] = oldTotal + partialTotal;
                } else if ("synchronized".equals(method)) {
                    synchronized (lock) {
                        sharedTotal[0] += partialTotal;
                    }
                } else {
                    atomicTotal.addAndGet(partialTotal);
                }
            });
            workers[i].start();
        }

        for (Thread worker : workers) {
            worker.join();
        }

        long total;
        if ("atomic".equals(method)) {
            total = atomicTotal.get();
        } else {
            total = sharedTotal[0];
        }
        return new SumResult(total, System.nanoTime() - startTime);
    }

    private static void printResult(String label, SumResult result, long expectedTotal) {
        System.out.println(label + ": " + result.total);
        System.out.println("  Correct: " + (result.total == expectedTotal));
        System.out.println("  Elapsed: " + result.elapsedNanos + " ns");
    }
}
