import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class MyTests {
    @Run
    public void testAddition() {
        System.out.println("  2 + 3 = " + (2 + 3));
    }

    @Run
    public void testStringLength() {
        System.out.println("  length of hello = " + "hello".length());
    }

    @Run
    public void testMax() {
        System.out.println("  max of 4 and 9 = " + Math.max(4, 9));
    }

    public void helperMethod() {
        System.out.println("  I should NOT run");
    }

    public void anotherHelper() {
        System.out.println("  I should NOT run either");
    }
}

public class MiniTestRunner {
    public static void main(String[] args) throws Exception {
        Class<?> cls = MyTests.class;
        Object obj = cls.getDeclaredConstructor().newInstance();

        int count = 0;

        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Run.class)) {
                System.out.println("Running " + method.getName());
                method.invoke(obj);
                count++;
            }
        }

        System.out.println("Total methods run: " + count);
    }
}