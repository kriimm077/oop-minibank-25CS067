import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class LogAnalyzer {

    private static final String KEYWORD = "ERROR";

    public static void main(String[] args) {
        Path logDirectory = Paths.get("logs");

        try {
            Files.createDirectories(logDirectory);
            createSampleLogs(logDirectory);
            analyzeLogs(logDirectory);
        } catch (IOException exception) {
            System.out.println("Could not analyze log files: " + exception.getMessage());
        }
    }

    private static void createSampleLogs(Path logDirectory) throws IOException {
        List<String> firstLog = Arrays.asList(
                "INFO Application started",
                "ERROR Could not connect to the service",
                "INFO Retrying connection"
        );
        List<String> secondLog = Arrays.asList(
                "INFO User signed in",
                "WARNING Disk space is low",
                "ERROR Request timed out",
                "INFO Application stopped"
        );
        List<String> thirdLog = Arrays.asList(
                "INFO Scheduled task completed",
                "error Cache was unavailable"
        );

        Files.write(logDirectory.resolve("app-one.log"), firstLog);
        Files.write(logDirectory.resolve("app-two.log"), secondLog);
        Files.write(logDirectory.resolve("app-three.log"), thirdLog);
    }

    private static void analyzeLogs(Path logDirectory) throws IOException {
        int[] lineCounts = {0, 0};

        try (Stream<Path> paths = Files.list(logDirectory)) {
            for (Path file : paths.filter(Files::isRegularFile).sorted().toArray(Path[]::new)) {
                BasicFileAttributes attributes = Files.readAttributes(
                        file, BasicFileAttributes.class);
                System.out.println(file.getFileName() + " size: "
                        + attributes.size() + " bytes");

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lineCounts[0]++;
                        if (line.toUpperCase(Locale.ROOT).contains(KEYWORD)) {
                            lineCounts[1]++;
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Total lines across all log files: " + lineCounts[0]);
        System.out.println("Lines containing \"" + KEYWORD + "\": " + lineCounts[1]);
    }
}
