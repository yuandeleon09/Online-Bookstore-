package FINALNAAYOKONA;

public class Book {
    private int bookId;
    private String name;
    private String author;
    private double price;
    private byte[] imageData;

    // Constructor
    public Book(int bookId, String name, String author, double price, byte[] imageData) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.price = price;
        this.imageData = imageData;
    }

    // Constructor without image data (if needed)
    public Book(int bookId, String name, String author, double price) {
        this(bookId, name, author, price, null);
    }

    // Constructor for cart and favorites (if image data is not always needed)
    public Book(int bookId, String name, double price) {
        this(bookId, name, null, price, null);
    }

    // Getter methods
    public int getBookId() {
        return bookId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public byte[] getImageData() {
        return imageData;
    }

    // Setter methods (if you need to modify book properties later)
    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    // Optionally, override equals and hashCode for proper comparison in collections
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(bookId);
    }

	public Comparable<?> getStock() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
