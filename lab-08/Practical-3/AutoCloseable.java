public class AutoCloseable {

    // A small stand-in for a resource such as a file or database connection
    private static class DemoResource implements java.lang.AutoCloseable {
        @Override
        public void close() {
            System.out.println("Resource closed automatically.");

            // Simulate a problem that happens while closing the resource
            throw new IllegalStateException("Simulated error while closing the resource.");
        }
    }

    public static void main(String[] args) {
        try (DemoResource resource = new DemoResource()) {
            System.out.println("Using the resource...");

            // This is the original error that should be reported
            throw new IllegalStateException("Simulated error inside the try block.");
        } catch (IllegalStateException exception) {
            System.out.println("Reported error: " + exception.getMessage());

            // A close error is saved as a suppressed exception, not used instead
            for (Throwable suppressed : exception.getSuppressed()) {
                System.out.println("Suppressed close error: " + suppressed.getMessage());
            }
        }
    }
}
