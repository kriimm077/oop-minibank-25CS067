public class BoundedStats {

    private static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Cannot find the average of an empty array.");
        }

        double total = 0;
        for (N value : values) {
            total += value.doubleValue();
        }
        return total / values.length;
    }

    public static void main(String[] args) {
        Integer[] integers = {2, 4, 6, 8};
        Double[] doubles = {1.5, 2.5, 3.5};
        Long[] longs = {10L, 20L, 30L};

        System.out.println("Integer average: " + average(integers));
        System.out.println("Double average: " + average(doubles));
        System.out.println("Long average: " + average(longs));
        System.out.println("The N extends Number bound allows numeric conversions, "
                + "and prevents non-number types such as String from being passed.");
    }
}
