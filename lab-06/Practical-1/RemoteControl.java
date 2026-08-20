interface Switchable{



void on();
void off();
 boolean isOn();
 default void toggle(){
if(isOn()){
    off();
}
else{
    on();
}

    }
}

    interface SwitchOnRule
    {
            boolean maySwitchOn(Switchable Device,int hour);
          
    }
        
    


class Light implements Switchable{

        boolean state=false;
        public void on(){
            state=true;
            System.out.println("Light on");
        }
    public void off(){
            state=false;
            System.out.println("Light off");
        }

        public boolean isOn(){

            return state;
        }
       
        }


class Fan implements Switchable{

        boolean state=false;
        public void on(){
            state=true;
            System.out.println("Fan on");
        }
    public void off(){
            state=false;
            System.out.println("Fan off");
        }


        public boolean isOn(){

            return state;
        }
        }

class RemoteControl
{



    public static void main(String[] args){
  
  Switchable[] devices={new Light(), new Fan()};

    for(Switchable d: devices)
    {
    d.toggle();
    
    }

    //DayTimeRule rule=new DayTimeRule();
    
    SwitchOnRule rule=new SwitchOnRule()
    {
        public boolean maySwitchOn(Switchable Device,int hour)
        {
            return (hour>=6 && hour<=22);
        }
    };


    if(rule.maySwitchOn(devices[0], 24))
    {
        devices[0].on();
    }
    else
    {
        System.out.println("Cannot switch on lights at this time");

    }
}
}