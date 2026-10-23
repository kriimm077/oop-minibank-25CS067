package service;

import exception.InsufficientFundsException;
import java.nio.file.Path;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankServiceTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void depositIncreasesBalance() throws Exception {
        try (BankService service = new BankService(temporaryDirectory)) {
            var account = service.openAccount(
                    "Asha", "asha@example.com", "9876543210", 1_000);

            service.deposit(account.getAccountNumber(), 250);

            assertEquals(1_250, account.getBalance());
        }
    }

    @Test
    void withdrawalOverAvailableBalanceThrows() throws Exception {
        try (BankService service = new BankService(temporaryDirectory)) {
            var account = service.openAccount(
                    "Ravi", "ravi@example.com", "9876543211", 1_000);

            assertThrows(InsufficientFundsException.class,
                    () -> service.withdraw(account.getAccountNumber(), 1_001));
        }
    }

    @Test
    void concurrentDepositsKeepExactBalance() throws Exception {
        try (BankService service = new BankService(temporaryDirectory)) {
            var account = service.openAccount(
                    "Mina", "mina@example.com", "9876543212", 1_000);
            ArrayList<BankService.TransactionRequest> batch = new ArrayList<>();
            for (int i = 0; i < 500; i++) {
                batch.add(new BankService.TransactionRequest(
                        account.getAccountNumber(), AccountTransaction.Type.DEPOSIT, 1));
            }

            service.processBatch(batch);

            assertEquals(1_500, account.getBalance());
        }
    }
}
