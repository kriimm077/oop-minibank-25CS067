import java.util.Scanner;

public class GuardedCalculator {

    private static class DivideByZeroException extends Exception {
        public DivideByZeroException() {
            super("You cannot divide by zero.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int attempt = 0;
        boolean calculationSucceeded = false;

        System.out.println("Welcome to the guarded calculator!");

        while (!calculationSucceeded) {
            attempt++;
            calculationSucceeded = false;

            try {
                System.out.print("Enter the first number: ");
                double firstNumber = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Enter an operator (+, -, *, /): ");
                String operator = scanner.nextLine().trim();

                System.out.print("Enter the second number: ");
                double secondNumber = Double.parseDouble(scanner.nextLine().trim());

                double result;

                switch (operator) {
                    case "+":
                        result = firstNumber + secondNumber;
                        break;
                    case "-":
                        result = firstNumber - secondNumber;
                        break;
                    case "*":
                        result = firstNumber * secondNumber;
                        break;
                    case "/":
                        if (secondNumber == 0) {
                            throw new DivideByZeroException();
                        }
                        result = firstNumber / secondNumber;
                        break;
                    default:
                        System.out.println("Invalid operator. Please enter +, -, * or /.");
                        continue;
                }

                System.out.println("Result: " + result);
                calculationSucceeded = true;
            } catch (NumberFormatException exception) {
                System.out.println("Invalid number. Please enter a valid number, such as 12 or 3.5.");
            } catch (DivideByZeroException exception) {
                System.out.println(exception.getMessage());
            } finally {
                System.out.println("Attempt " + attempt + " finished.");
                System.out.println();
            }
        }

        System.out.println("Calculation complete. Goodbye!");
        scanner.close();
    }
}
