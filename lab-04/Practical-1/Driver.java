import java.util.*;

public class Driver{


    public static void main(String[] args) {
      System.out.println("Please enter password to check");  
      Scanner sc= new Scanner(System.in);
      String password=sc.next();
    
    System.out.println(PasswordChecker.getStrength(password));
    
    
    
    
    }





}