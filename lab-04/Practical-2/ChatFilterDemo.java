
import java.util.*;
public class ChatFilterDemo{



 public static void main(String[] args) {
       String[] log={"04:04 Luffy  Hello One Piece is peak" , "04:05 Zoro  Hello I am Bounty Hunter" , "04:06 Krima "};
       
       System.out.println("Enter Keyword:");
        Scanner sc = new Scanner(System.in);
         String keyword = sc.next();  
      
      int match=0;
      StringBuilder report=new StringBuilder();
       for (String msg : log) {
         ChatFilter.splitLine(msg);
        String[]parts = ChatFilter.splitLine(msg);
        if(parts==null)
         continue;



        String time=parts[0];
        String name=parts[1];
        String msgs=parts[2];
       
     if(ChatFilter.hasKeyword(msgs,keyword)){
        match++;
        report.append(time).append(" ").append(name).append(": ").append(msgs).append("\n");
     }
    
    
             }
             System.out.println("matches: " + match);
            System.out.println("matches: " + report);
           
  
    
 }


    }

class ChatFilter{
    public static String[] splitLine(String line){
        String[]parts=line.split(" ",3);
    if (parts.length<=2){ return null;}
    

    return parts;
    
    }
    public static boolean hasKeyword(String msg,String keyword){
      return msg.toLowerCase().contains(keyword.toLowerCase());
    
    
    }
   
   
    }

    

   