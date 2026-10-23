package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;

public class AccountStore {
    private final Map<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null.");
        }

        String accountNumber = account.getAccountNumber();
        if (accounts.putIfAbsent(accountNumber, account) != null) {
            throw new IllegalArgumentException(
                    "An account with number " + accountNumber + " already exists.");
        }
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sortedAccounts = getAll();
        sortedAccounts.sort(Comparator.comparingLong(Account::getBalance)
                .thenComparing(Account::getAccountNumber));
        return sortedAccounts;
    }

    public void addTransaction(String transaction) {
        if (transaction == null || transaction.isBlank()) {
            throw new IllegalArgumentException("Transaction history entry must not be blank.");
        }
        transactionHistory.add(transaction);
    }

    public List<String> getTransactionHistory() {
        return List.copyOf(transactionHistory);
    }
}
