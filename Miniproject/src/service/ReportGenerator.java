package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ReportGenerator {
    private ReportGenerator() {
    }

    public static void generate(Path logsFolder, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        List<String> fileDetails = new ArrayList<>();

        Files.createDirectories(logsFolder);
        try (DirectoryStream<Path> files = Files.newDirectoryStream(logsFolder)) {
            for (Path file : files) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }

                BasicFileAttributes attributes = Files.readAttributes(
                        file, BasicFileAttributes.class);
                fileDetails.add(file.getFileName() + " | " + attributes.size()
                        + " bytes | modified " + attributes.lastModifiedTime());

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] fields = line.trim().split("\\s+");
                        if (fields.length < 2 || fields.length > 3) {
                            throw new IOException("Invalid transaction log entry in "
                                    + file.getFileName() + ": " + line);
                        }

                        long amount;
                        try {
                            amount = Long.parseLong(fields[1]);
                        } catch (NumberFormatException exception) {
                            throw new IOException("Invalid transaction amount in "
                                    + file.getFileName() + ": " + line, exception);
                        }
                        if (amount < 0) {
                            throw new IOException("Transaction amount must not be negative: "
                                    + line);
                        }

                        switch (fields[0]) {
                            case "DEPOSIT" -> totalDeposits += amount;
                            case "WITHDRAW" -> totalWithdrawals += amount;
                            default -> throw new IOException("Unknown transaction type in "
                                    + file.getFileName() + ": " + fields[0]);
                        }
                    }
                }
            }
        }

        fileDetails.sort(Comparator.naturalOrder());
        List<String> report = new ArrayList<>();
        report.add("MiniBank End-of-Day Report");
        report.add("Total deposits: " + totalDeposits);
        report.add("Total withdrawals: " + totalWithdrawals);
        report.add("Log files:");
        report.addAll(fileDetails);

        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(reportFile, report);
    }
}
