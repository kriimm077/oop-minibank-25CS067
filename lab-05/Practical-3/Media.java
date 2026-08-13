public abstract class Media {

    protected String title;
    protected int daysLate;

    public Media(String title, int daysLate) {
        this.title = title;
        this.daysLate = daysLate;
    }

    public abstract double lateFee();

    public String getTitle() {
        return title;
    }

    public int getDaysLate() {
        return daysLate;
    }
}



class Book extends Media {

    public Book(String title, int daysLate) {
        super(title, daysLate);
    }

  
    public double lateFee() {
        return daysLate * 2;
    }
}




class CD extends Media {

    public CD(String title, int daysLate) {
        super(title, daysLate);
    }

    public double lateFee() {
        return daysLate * 5;
    }
}




class Magazine extends Media {

    public Magazine(String title, int daysLate) {
        super(title, daysLate);
    }

  
    public double lateFee() {
        return daysLate * 3;
    }
}




class MediaDemo {

    public static void main(String[] args) {

        Media[] returnedMedia = {
            new Book("Java Programming", 4),
            new CD("One Piece OST", 3),
            new Magazine("Tech Today", 5)
        };

        double totalFee = 0;

        for (Media media : returnedMedia) {

            double fee = media.lateFee();

            System.out.println(
                media.getTitle()
                + " | Days late: "
                + media.getDaysLate()
                + " | Late fee: ₹"
                + fee
            );

            totalFee += fee;
        }

        System.out.println("------------------------------");
        System.out.println("Total late fee: ₹" + totalFee);
    }
}