import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {

    public static void main(String[] args) {
        String paragraph = "Java makes programming fun. Java is useful, and programming "
                + "with Java can be fun.";

        Map<String, Integer> wordCounts = new HashMap<>();
        String[] words = paragraph.toLowerCase().split("[^a-z0-9]+");

        for (String word : words) {
            if (!word.isEmpty()) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedWords =
                new ArrayList<>(wordCounts.entrySet());
        sortedWords.sort(
                Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey)
        );

        System.out.println("Words sorted by frequency (highest first):");
        for (Map.Entry<String, Integer> entry : sortedWords) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
