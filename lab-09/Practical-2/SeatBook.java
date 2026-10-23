import java.util.concurrent.CountDownLatch;

public class SeatBook {

    private static final int AVAILABLE_SEATS = 5;
    private static final int THREAD_COUNT = 10;

    private int seatsLeft = AVAILABLE_SEATS;

    // This version is intentionally unsafe: checking and booking are separate steps
    private boolean bookWithoutSynchronization(CountDownLatch allThreadsChecked)
            throws InterruptedException {
        if (seatsLeft <= 0) {
            return false;
        }

        // Wait until every thread has checked before allowing any thread to book
        allThreadsChecked.countDown();
        allThreadsChecked.await();

        seatsLeft--;
        return true;
    }

    // The synchronized method makes checking and booking one indivisible operation
    private synchronized boolean book() {
        if (seatsLeft <= 0) {
            return false;
        }

        seatsLeft--;
        return true;
    }

    private static int runBookingAttempts(boolean useSynchronization)
            throws InterruptedException {
        SeatBook seatBook = new SeatBook();
        boolean[] bookingResults = new boolean[THREAD_COUNT];
        CountDownLatch allThreadsChecked = new CountDownLatch(THREAD_COUNT);
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    if (useSynchronization) {
                        bookingResults[threadIndex] = seatBook.book();
                    } else {
                        bookingResults[threadIndex] =
                                seatBook.bookWithoutSynchronization(allThreadsChecked);
                    }
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int successfulBookings = 0;
        for (boolean bookingSucceeded : bookingResults) {
            if (bookingSucceeded) {
                successfulBookings++;
            }
        }
        return successfulBookings;
    }

    public static void main(String[] args) throws InterruptedException {
        int unsafeBookings = runBookingAttempts(false);
        System.out.println("Without synchronization, successful bookings: " + unsafeBookings);
        System.out.println("Seats available: " + AVAILABLE_SEATS);

        int safeBookings = runBookingAttempts(true);
        System.out.println();
        System.out.println("With synchronized book(), successful bookings: " + safeBookings);
        System.out.println("Seats available: " + AVAILABLE_SEATS);

        if (safeBookings == AVAILABLE_SEATS) {
            System.out.println("Synchronized booking count is correct.");
        }
    }
}
