package app;

import model.DiscountRule;
import service.DiscountEngine;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Double> prices = new ArrayList<>();

        System.out.print("Enter number of prices: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter price " + (i + 1) + ": ");
            prices.add(sc.nextDouble());
        }

        System.out.println("\nChoose discount rule:");
        System.out.println("1. 10% discount");
        System.out.println("2. 20% discount");
        System.out.println("3. Flat ₹100 discount");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        DiscountRule rule;

        switch (choice) {
            case 1:
                rule = price -> price * 0.90;
                break;

            case 2:
                rule = price -> price * 0.80;
                break;

            case 3:
                rule = price -> Math.max(0, price - 100);
                break;

            default:
                System.out.println("Invalid choice.");
                sc.close();
                return;
        }

        System.out.println("\nDiscounted prices:");
        DiscountEngine.applyDiscount(prices, rule);

        sc.close();
    }
}