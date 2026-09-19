package service;

import model.Account;
import model.TransactionType;
public record Command(
        TransactionType type,
        String accountNumber,
        long amount
) {
}