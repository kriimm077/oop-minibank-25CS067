package model;

import java.io.Serial;
import java.io.Serializable;
import util.Premium;

public class FixedDepositAccount extends Account implements Premium, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public FixedDepositAccount(
            String ownerName,
            long openingBalance) {

        super(ownerName, openingBalance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return false;
    }

    @Override
    protected long availableToWithdraw() {
        return 0;
    }
}