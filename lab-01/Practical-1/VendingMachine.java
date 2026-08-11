

import java.util.*;

class VendingMachine{

enum Coin{ONE,TWO,FIVE,TEN}
public static void main(String[]args)
{


final int PRICE =15;


int total = 0;
int value=0;
Coin c=null;
  Scanner sc = new Scanner(System.in);



while(total<=PRICE){
System.out.println("Enter coins");
String input =sc.nextLine();
try{
c=Coin.valueOf(input.toUpperCase());
}

catch (Exception e){
System.out.println("invalid coin , try again.");
continue;

}
switch(c){
case ONE:
value=1;
break;

case TWO:
value=2;
break;

case FIVE:
value=5;
break;

case TEN:
value=10;
break;


}
total=total+value;
System.out.println("total: " + total);

}

System.out.println("total: " + total);
System.out.println("total: " + total + "diff" + (total-PRICE));


}


}
