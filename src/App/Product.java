package App;

public class Product {
    public String ownerEmail;
    public int productID;
    public int packageID;
    public int price;
    public String category;
    public String description;

    public Product(String ownerEmail, int productID, int packageID, int price, String category) {
        this.ownerEmail = ownerEmail;
        this.productID = productID;
        this.packageID = packageID;
        this.price = price;
        this.category = category;
        description = "";
    }


}
