package model;

import java.io.Serial;
import java.io.Serializable;

public class Customer implements Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String mobile;

    private final String customerId;

    private Address address;

    private static long customerCounter = 100;

    private static String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public Customer(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.customerId = generateCustomerId();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCustomerId() {
        return customerId;
    }

    // Get the customer's address
    public Address getAddress() {
        return address;
    }

    // Set the customer's address
    public void setAddress(Address address) {
        this.address = address;
    }

    // Clone the customer
    @Override
    public Customer clone() {
        try {
            Customer copy = (Customer) super.clone();

            // Create a separate copy of Address
            if (this.address != null) {
                copy.address = new Address(
                    address.getLine(),
                    address.getCity(),
                    address.getPincode()
                );
            }

            return copy;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Static nested Address class
    public static class Address implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }
    }
}