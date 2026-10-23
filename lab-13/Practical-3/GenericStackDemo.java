import java.util.Arrays;
import java.util.EmptyStackException;

public class GenericStackDemo {

    private static class ArrayStack<T> {
        private Object[] items;
        private int size;

        public ArrayStack(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be greater than zero.");
            }
            items = new Object[capacity];
        }

        public void push(T item) {
            if (size == items.length) {
                items = Arrays.copyOf(items, items.length * 2);
            }
            items[size++] = item;
        }

        @SuppressWarnings("unchecked")
        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }

            int topIndex = --size;
            T item = (T) items[topIndex];
            items[topIndex] = null;
            return item;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }

    public static void main(String[] args) {
        ArrayStack<String> words = new ArrayStack<>(2);
        words.push("first");
        words.push("second");

        ArrayStack<Integer> numbers = new ArrayStack<>(2);
        numbers.push(10);
        numbers.push(20);

        System.out.println("Word stack, last item first: " + words.pop() + ", " + words.pop());
        System.out.println("Number stack, last item first: "
                + numbers.pop() + ", " + numbers.pop());

        try {
            words.pop();
        } catch (EmptyStackException exception) {
            System.out.println("Cannot pop: the word stack is empty.");
        }
    }
}
