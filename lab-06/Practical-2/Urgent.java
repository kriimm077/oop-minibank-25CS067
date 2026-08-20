interface Urgent{}

 
 interface Notifier
 {

    void send(String msg);


 }

 class Sms implements Notifier,Urgent
 {

    public void send(String message)
    {
        System.out.println("SMS " + message);
    }

 }


class Email implements Notifier
{
    public void send(String message)
    {
        System.out.println("Email " + message);
    }
}

class Broadcast{
public static void main(String[] args)
    {
        Notifier[] notifiers = {new Sms(), new Email()};
        for(Notifier n : notifiers)
        {
            n.send("Server Down");
            if(n instanceof Urgent){
                n.send("Urgent:Server down!!");
            }
        }
    }
    
}