package View.UserViewScreen;

import App.Address;
import App.Product;


public class ProductEntry
{
    private String ownerEmail;
    private int productID;
    private int packageID;
    private int price;
    private String category;
    private String availability;
    private String description;
    //    private String address;
//    private Address address;
    private String neighborhood;
    private String city;
    private String street;
    private String startDate;
    private String endDate;

    public ProductEntry(Product p) {
        ownerEmail = p.ownerEmail;
        productID = p.productID;
        packageID = p.packageID;
        description = p.description;
        price = p.price;
        category = p.category;
        neighborhood = "";
        city = "";
        street = "";
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ProductEntry() {
        category ="";
        neighborhood = "";
        city = "";
        street = "";
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Address getAddress() {
        return new Address(city, neighborhood, street);
    }

    public void setAddress(Address address) {
        city = address.getCity();
        neighborhood = address.getNeighborhood();
        street = address.getStreet();
    }

    public String getAvailability() {
        return startDate + "-" + endDate;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
