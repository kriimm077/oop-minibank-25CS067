class ParkingLot {

    private int twoWheelers;
    private int fourWheelers;

    private final int twoCap;
    private final int fourCap;

    private static long revenue = 0;

    ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
        this.twoWheelers = 0;
        this.fourWheelers = 0;
    }

    public void park(String type) {

        if (type.equals("two")) {

            if (twoWheelers < twoCap) {
                twoWheelers++;
                revenue += 20;
                System.out.println("Two-wheeler parked");
            } else {
                System.out.println("Full");
            }

        } else if (type.equals("four")) {

            if (fourWheelers < fourCap) {
                fourWheelers++;
                revenue += 40;
                System.out.println("Four-wheeler parked");
            } else {
                System.out.println("Full");
            }

        } else {
            System.out.println("Invalid vehicle type");
        }
    }

    public void leave(String type) {

        if (type.equals("two")) {

            if (twoWheelers > 0) {
                twoWheelers--;
                System.out.println("Two-wheeler left");
            }

        } else if (type.equals("four")) {

            if (fourWheelers > 0) {
                fourWheelers--;
                System.out.println("Four-wheeler left");
            }

        } else {
            System.out.println("Invalid vehicle type");
        }
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot(2, 2);

        lot.park("two");
        lot.park("two");
        lot.park("two");

        lot.park("four");
        lot.park("four");
        lot.park("four");

        lot.leave("two");
        lot.leave("four");

        lot.park("two");
        lot.park("four");

        System.out.println("Final two-wheelers: " + lot.twoWheelers);
        System.out.println("Final four-wheelers: " + lot.fourWheelers);
        System.out.println("Final revenue: " + revenue);
    }
}