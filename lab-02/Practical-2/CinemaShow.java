class CinemaShow {

    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;

    CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    CinemaShow(String title) {
        this(title, 100);
    }

    public boolean book(int n) {
        if (n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        } else {
            return false;
        }
    }

    public void cancel(int n) {
        seatsAvailable += n;

        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args) {

        CinemaShow show = new CinemaShow("Avengers", 100);

        System.out.println("Book 30: " + show.book(30));
        System.out.println("Seats available: " + show.getSeatsAvailable());

        System.out.println("Book 50: " + show.book(50));
        System.out.println("Seats available: " + show.getSeatsAvailable());

        System.out.println("Book 30: " + show.book(30));
        System.out.println("Seats available: " + show.getSeatsAvailable());

        show.cancel(20);
        System.out.println("After cancelling 20:");
        System.out.println("Seats available: " + show.getSeatsAvailable());

        System.out.println("Book 10: " + show.book(10));
        System.out.println("Seats available: " + show.getSeatsAvailable());

        System.out.println("Total booked: " + CinemaShow.getTotalBooked());
    }
}