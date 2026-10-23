import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Leaderboard {

    private static final int TOP_N = 3;

    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Asha", 950);
        scores.put("Ravi", 820);
        scores.put("Mina", 950);
        scores.put("Omar", 760);
        scores.put("Zara", 880);

        List<Map.Entry<String, Integer>> leaderboard = scores.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(TOP_N)
                .collect(Collectors.toList());

        System.out.println("Top " + TOP_N + " players (score highest first, "
                + "ties by name A-Z):");
        for (int i = 0; i < leaderboard.size(); i++) {
            Map.Entry<String, Integer> player = leaderboard.get(i);
            System.out.println((i + 1) + ". " + player.getKey()
                    + " - " + player.getValue());
        }
    }
}
