import java.util.ArrayDeque;
import java.util.Queue;

public class ProducerConsumer {

    private static final int ITEM_COUNT = 10;
    private static final int BUFFER_CAPACITY = 3;

    private static class SharedBuffer {
        private final Queue<Integer> items = new ArrayDeque<>();

        public synchronized void put(int item) throws InterruptedException {
            while (items.size() == BUFFER_CAPACITY) {
                wait();
            }

            items.add(item);
            System.out.println("Produced: " + item);
            notifyAll();
        }

        public synchronized int take() throws InterruptedException {
            while (items.isEmpty()) {
                wait();
            }

            int item = items.remove();
            System.out.println("Consumed: " + item);
            notifyAll();
            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SharedBuffer buffer = new SharedBuffer();

        Thread producer = new Thread(() -> {
            try {
                for (int item = 1; item <= ITEM_COUNT; item++) {
                    buffer.put(item);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.out.println("Producer was interrupted.");
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int count = 0; count < ITEM_COUNT; count++) {
                    buffer.take();
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer was interrupted.");
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
        System.out.println("Finished: produced and consumed " + ITEM_COUNT + " items.");
    }
}
