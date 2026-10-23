import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRunner {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        try {
            for (int taskId = 1; taskId <= 10; taskId++) {
                final int id = taskId;
                pool.submit(() -> {
                    System.out.println("Task " + id + " is running on "
                            + Thread.currentThread().getName());
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        System.out.println("Task " + id + " was interrupted.");
                    }
                });
            }

            pool.shutdown();
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Tasks did not finish in time; stopping the pool.");
                pool.shutdownNow();
            }
        } catch (InterruptedException exception) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
            System.out.println("Main thread was interrupted while waiting for tasks.");
        }
    }
}
