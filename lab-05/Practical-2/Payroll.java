        abstract class Employee{
    abstract float monthlySalary();
    int ID;
    String name;
Employee(int ID, String name){
    this.ID=ID;
    this.name=name;
}
        }     


 class FullTime extends Employee{
    int salary;
    FullTime(int ID, String name, int salary){
        super(ID, name);
        this.salary=salary;
    }
    public float monthlySalary(){
        return salary;
    }
    
}

class PartTime extends Employee{
    int Rate;
    int hours;
    PartTime(int ID, String name, int Rate, int hours){
        super(ID, name);
        this.Rate=Rate;
        this.hours=hours;
    }
    public float monthlySalary(){
        return (float) (Rate * hours);
    }

}

class Intern extends Employee{
    int stipend;
    Intern(int ID, String name, int stipend){
        super(ID, name);
        this.stipend=stipend;
    }
    public float monthlySalary(){
        return (float) stipend;
    }
}


public class Payroll{
    public static void main(String args[]){
        Employee[] employees = {
            new FullTime(1, "Krima", 5000),
            new PartTime(2, "Clover", 20, 80),
            new Intern(3, "TaylorSwift", 1000)
        };

        for(Employee e: employees){
            System.out.println(e.monthlySalary());
            if(e instanceof Intern){
                System.out.println("Interns have stipend, not salary");
            }
        }
    }
}