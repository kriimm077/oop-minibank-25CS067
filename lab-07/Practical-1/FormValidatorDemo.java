import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    @MaxLength(8)
    String username;

    @NotBlank
    String email;

    @MaxLength(5)
    String referralCode;

    SignupForm(String username, String email, String referralCode) {
        this.username = username;
        this.email = email;
        this.referralCode = referralCode;
    }
}

class FormChecker {
    static List<String> check(Object obj) throws IllegalAccessException {
        List<String> errors = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);
            String text = (value == null) ? "" : value.toString();

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (text.trim().isEmpty()) {
                    errors.add(field.getName() + " must not be blank");
                }
            }

            if (field.isAnnotationPresent(MaxLength.class)) {
                int max = field.getAnnotation(MaxLength.class).value();
                if (text.length() > max) {
                    errors.add(field.getName() + " must be at most " + max + " characters");
                }
            }
        }

        return errors;
    }
}

public class FormValidatorDemo {
    public static void main(String[] args) throws Exception {
        SignupForm bad = new SignupForm("   ", "", "ABCDEFGH");
        SignupForm good = new SignupForm("rahul", "rahul@mail.com", "AB12");

        System.out.println("Bad form errors:");
        for (String error : FormChecker.check(bad)) {
            System.out.println(" - " + error);
        }

        System.out.println("Good form errors:");
        List<String> goodErrors = FormChecker.check(good);
        if (goodErrors.isEmpty()) {
            System.out.println(" none");
        }
    }
}