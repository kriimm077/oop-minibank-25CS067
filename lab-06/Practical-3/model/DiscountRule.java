package model;

@FunctionalInterface
public interface DiscountRule {
    double apply(double price);
}