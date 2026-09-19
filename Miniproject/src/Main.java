import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;

public class Main {

    public static void main(String[] args) {

        Account savings = new SavingsAccount(
                "Riya",
                5000,
                2000
        );

        Account current = new CurrentAccount(
                "Aman",
                4000,
                2000
        );

        Account fixedDeposit = new FixedDepositAccount(
                "Jagruti",
                10000
        );

        Account[] accounts = {
            savings,
            current,
            fixedDeposit
        };

        for (Account account : accounts) {

            System.out.println(
                    account.getOwnerName()
                    + " | "
                    + account.getClass().getSimpleName()
                    + " | "
                    + account.interestRate()
                    + "%"
            );
        }

        if (savings instanceof SavingsAccount savingsAccount) {

            System.out.println(
                    "Minimum balance: "
                    + savingsAccount.getMinBalance()
            );
        }
    }
}