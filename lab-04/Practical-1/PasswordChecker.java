class PasswordChecker{



    public static boolean hasLength(String pw){


return (pw.length()>=8);


}

    public static boolean upperCase(String pw){


return (pw.matches(".*[A-Z].*"));


}


    public static boolean hasDigit(String pw){


    return (pw.matches(".*[0-9].*"));


    }   


    public static boolean hasSpecialChar(String pw){


    return (pw.matches(".*[_@*$].*"));


}


public static String getStrength(String pw){

int count=0;

if(hasLength(pw)){count ++;}

if(upperCase(pw)){count ++;}

if(hasDigit(pw)){count ++;}

if(hasSpecialChar(pw)){count ++;}



String strength = switch(count){

case 0,1 -> "Weak";
case 2,3 -> "Medium";
case 4 -> "Strong";
default -> "Invalid format";
};

return strength;
}



public static void main(String[] args) {
    
}

}