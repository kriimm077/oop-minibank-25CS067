package util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern MOBILE =
            Pattern.compile("^[6-9][0-9]{9}$");

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PAN =
            Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");

    private static final Pattern IFSC =
            Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");


    public static boolean isValidMobile(String mobile) {
        if (mobile == null) {
            return false;
        }
        Matcher matcher = MOBILE.matcher(mobile);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPan(String pan) {
        if (pan == null) {
            return false;
        }
        Matcher matcher = PAN.matcher(pan);
        return matcher.matches();
    }

    public static boolean isValidIfsc(String ifsc) {
        if (ifsc == null) {
            return false;
        }
        Matcher matcher = IFSC.matcher(ifsc);
        return matcher.matches();
    }
}