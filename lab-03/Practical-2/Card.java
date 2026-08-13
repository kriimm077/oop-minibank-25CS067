import java.util.Objects;

public class Card {
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }


    public String toString() {
        return rank + " of " + suit;
    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Card other = (Card) obj;

        return Objects.equals(this.rank, other.rank)
                && Objects.equals(this.suit, other.suit);
    }

   
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}