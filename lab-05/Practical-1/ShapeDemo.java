abstract class Shape{
abstract float area();

}

        class Circle extends Shape{
        int radius;
        Circle(int radius){
        this.radius=radius;
}
    public float area(){

return (float) (3.14*radius*radius);

}
        }


class Rectangle extends Shape{
int length;
int breadth;

Rectangle(int length,int breadth){
 this.length=length;
 this.breadth=breadth;


}
public float area(){

return (float) (length*breadth);
}
}

class Triangle extends Shape{
int base;
int height;

Triangle(int base,int height){
 this.base=base;
 this.height=height;



}
 public float area(){

return (float) (0.5*base*height);
}
}


        public class ShapeDemo{
        public static void main(String args[]){

    Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 8),
           
};
double total=0;
double largest= -1;
String label="";


for(Shape s: shapes) {
    
    System.out.println("Area: " + s.area());
    total+=s.area();
    if(largest<s.area()){
        largest=s.area();
    
        label=s.getClass().getSimpleName();
        

    }
            }

            System.out.println("Total Area: " + total);
            System.out.println("Largest Area: " + largest + " of shape" + label);
            
}


}





