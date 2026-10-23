public class GenericContainers {

    private static class Box<T> {
        private T value;

        public void set(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }
    }

    private static class Pair<First, Second> {
        private final First first;
        private final Second second;

        public Pair(First first, Second second) {
            this.first = first;
            this.second = second;
        }

        public First getFirst() {
            return first;
        }

        public Second getSecond() {
            return second;
        }
    }

    private static <T> T firstElement(T[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Array must not be empty.");
        }
        return values[0];
    }

    public static void main(String[] args) {
        Box<String> nameBox = new Box<>();
        nameBox.set("Taylor");

        Box<Integer> scoreBox = new Box<>();
        scoreBox.set(95);

        Pair<String, Integer> student = new Pair<>("Jordan", 102);

        String[] colors = {"blue", "green", "red"};
        Integer[] numbers = {10, 20, 30};

        String firstColor = firstElement(colors);
        Integer firstNumber = firstElement(numbers);

        System.out.println("Name in box: " + nameBox.get());
        System.out.println("Score in box: " + scoreBox.get());
        System.out.println("Pair: " + student.getFirst() + ", " + student.getSecond());
        System.out.println("First color: " + firstColor);
        System.out.println("First number: " + firstNumber);
        System.out.println("The compiler checks each type, so no casts are needed.");
    }
}
