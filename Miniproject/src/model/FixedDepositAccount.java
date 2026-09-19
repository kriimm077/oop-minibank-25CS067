package model;

import util.Premium;
public class FixedDepositAccount extends Account implements Premium {

    public FixedDepositAccount(
            String ownerName,
            long openingBalance) {

        super(ownerName, openingBalance);
    }

    
    public double interestRate() {
        return 7.0;
    }

   
    public boolean canWithdraw(long amount) {
        return false;
    }
}