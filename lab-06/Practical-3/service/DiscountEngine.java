package service;

import model.DiscountRule;
import java.util.List;

public class DiscountEngine {

    public static void applyDiscount(List<Double> prices, DiscountRule rule) {
        for (double price : prices) {
            double finalPrice = rule.apply(price);
            System.out.println("Original: " + price + " -> Final: " + finalPrice);
        }
    }
}