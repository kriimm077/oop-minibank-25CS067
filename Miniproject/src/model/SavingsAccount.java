
package model;

import java.io.Serial;
import java.io.Serializable;
import util.*;


public class SavingsAccount extends Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private long minBalance;

    public SavingsAccount(
            String ownerName,
            long openingBalance,
            long minBalance) {

        super(ownerName, openingBalance);
        this.minBalance = minBalance;
    }
    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return getBalance() - amount >= minBalance;
    }

    @Override
    protected long availableToWithdraw() {
        return Math.max(0, getBalance() - minBalance);
    }

    public long getMinBalance() {
        return minBalance;
    }
}