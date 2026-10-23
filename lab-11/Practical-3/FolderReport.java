import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FolderReport {

    private static final String REPORT_NAME = "folder-report.txt";

    public static void main(String[] args) {
        Path folder = args.length > 0 ? Paths.get(args[0]) : Paths.get(".");
        Path report = folder.resolve(REPORT_NAME);

        try {
            Path root = folder.toAbsolutePath().normalize();
            Path reportPath = report.toAbsolutePath().normalize();
            List<String> entries = new ArrayList<>();

            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    if (!file.toAbsolutePath().normalize().equals(reportPath)) {
                        Path relativeName = root.relativize(file);
                        entries.add(relativeName + " | " + attributes.size() + " bytes | "
                                + Instant.ofEpochMilli(attributes.lastModifiedTime().toMillis()));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            entries.sort(Comparator.naturalOrder());
            List<String> reportLines = new ArrayList<>();
            reportLines.add("File report for: " + root);
            reportLines.add("Each entry includes the relative name, size, and modified time.");
            reportLines.addAll(entries);
            Files.write(reportPath, reportLines);

            System.out.println("Reported " + entries.size() + " file(s) in "
                    + reportPath);
        } catch (IOException exception) {
            System.out.println("Could not create the folder report: " + exception.getMessage());
        }
    }
}
