package FINALNAAYOKONA;

public class CartItem {
    private Book book;
    private int quantity;
    private String status; // Added status

    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
        this.status = "in cart"; // Default status
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
