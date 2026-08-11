import java.util.*;

record Vehicle(String number, String type) {
}

class TollBooth {

    public static void main(String[] a) {

        int totalToll = 0;
        int bike = 0;
        int car = 0;
        int truck = 0;

        Scanner sc = new Scanner(System.in);

        while (true) {
            String type = sc.next();
            if (type.equalsIgnoreCase("done"))
                break;

            String number = sc.next();
            Vehicle v = new Vehicle(number, type);
            int toll = 0;

            switch (v.type()) {
                case "bike":
                    toll = 20;
                    bike++;
                    break;

                case "car":
                    toll = 50;
                    car++;
                    break;

                case "truck":
                    toll = 150;
                    truck++;
                    break;

                default:
                    toll = 0;
            }

            System.out.println(v.type() + "passed with toll=" + toll);
            totalToll = totalToll + toll;
        }

        System.out.println("Total bikes passed =" + bike);
        System.out.println("Total cars passed =" + car);
        System.out.println("Total Trucks passed =" + truck);

        System.out.println("Total collected toll=" + totalToll);

        if (car >= bike && car >= truck)
            System.out.println("Most frequent: car");
        else if (bike >= car && bike >= truck)
            System.out.println("Most frequent: bike");
        else
            System.out.println("Most frequent: truck");
    }
}