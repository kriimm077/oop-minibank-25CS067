package persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import model.Account;

public final class StatePersister {
    private StatePersister() {
    }

    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            Object savedData;
            try {
                savedData = input.readObject();
            } catch (ClassNotFoundException exception) {
                throw new IOException("A saved account class could not be found.", exception);
            }

            if (!(savedData instanceof Account[] accounts)) {
                throw new IOException("The file does not contain an account array.");
            }
            return accounts;
        }
    }
}
