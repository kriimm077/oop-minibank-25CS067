import java.util.Arrays;
import java.util.List;

public class StockIssue {

    // A checked exception that keeps the missing quantity available to the caller
    private static class OutOfStockException extends Exception {
        private final int shortfall;

        public OutOfStockException(int shortfall) {
            super("Not enough stock. Short by " + shortfall + " item(s).");
            this.shortfall = shortfall;
        }

        public int getShortfall() {
            return shortfall;
        }
    }

    private static class InvalidQuantityException extends Exception {
        public InvalidQuantityException() {
            super("Quantity must be greater than zero.");
        }
    }

    private static class Warehouse {
        private int stock;

        public Warehouse(int startingStock) {
            stock = startingStock;
        }

        public void issue(String item, int quantity)
                throws OutOfStockException, InvalidQuantityException {
            if (quantity <= 0) {
                throw new InvalidQuantityException();
            }

            if (quantity > stock) {
                throw new OutOfStockException(quantity - stock);
            }

            stock -= quantity;
            System.out.println("Issued " + quantity + " " + item + "(s).");
        }

        public int getStock() {
            return stock;
        }
    }

    private static class Request {
        private final String item;
        private final int quantity;

        public Request(String item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(10);

        List<Request> requests = Arrays.asList(
                new Request("notebook", 4),
                new Request("pen", 8),
                new Request("folder", 0),
                new Request("marker", 3),
                new Request("pencil", -2)
        );

        for (Request request : requests) {
            try {
                warehouse.issue(request.item, request.quantity);
            } catch (OutOfStockException exception) {
                System.out.println("Could not issue " + request.item + ": "
                        + exception.getMessage());
                System.out.println("Shortfall: " + exception.getShortfall() + " item(s).");
            } catch (InvalidQuantityException exception) {
                System.out.println("Could not issue " + request.item + ": "
                        + exception.getMessage());
            }
        }

        System.out.println("All requests processed. Remaining stock: " + warehouse.getStock());
    }
}
