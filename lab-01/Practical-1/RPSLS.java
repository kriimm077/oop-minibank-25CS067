import java.util.Scanner;
import java.util.Random;

class RPSLS {

    enum Move {
        ROCK, PAPER, SCISSORS, LIZARD, SPOCK
    }

    public static int winner(Move a, Move b) {

        if (a == b) {
            return 0;
        }

        return switch (a) {
            case SCISSORS ->
                (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;

            case PAPER ->
                (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1;

            case ROCK ->
                (b == Move.LIZARD || b == Move.SCISSORS) ? 1 : -1;

            case LIZARD ->
                (b == Move.SPOCK || b == Move.PAPER) ? 1 : -1;

            case SPOCK ->
                (b == Move.SCISSORS || b == Move.ROCK) ? 1 : -1;
        };
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playerScore = 0;
        int computerScore = 0;

        for (int round = 1; round <= 5; round++) {

            System.out.println("\nRound " + round);
            System.out.print("Enter your move (ROCK, PAPER, SCISSORS, LIZARD, SPOCK): ");

            Move playerMove = Move.valueOf(sc.next().toUpperCase());

            Move computerMove = Move.values()[random.nextInt(5)];

            System.out.println("Your move: " + playerMove);
            System.out.println("Computer move: " + computerMove);

            int result = winner(playerMove, computerMove);

            if (result == 1) {
                System.out.println("You win this round!");
                playerScore++;
            } 
            else if (result == -1) {
                System.out.println("Computer wins this round!");
                computerScore++;
            } 
            else {
                System.out.println("Tie!");
            }
        }

        System.out.println("\nFinal Score: You " + playerScore + " - " + computerScore + " Computer");

        if (playerScore > computerScore) {
            System.out.println("You win " + playerScore + "-" + computerScore);
        } 
        else if (computerScore > playerScore) {
            System.out.println("Computer wins " + computerScore + "-" + playerScore);
        } 
        else {
            System.out.println("It's a tie!");
        }

        sc.close();
    }
}