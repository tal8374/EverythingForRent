package App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Package {

    private List<Product> products;
    private String owner_email;
    private int package_id;
    private int total_price;
    private String cancellation_policy;
    private Address address;
    private LocalDate startDate;
    private LocalDate endDate;

    public boolean equals(Package obj) {
        return owner_email.equals(obj.owner_email) && package_id == obj.package_id &&
                products.size() == obj.products.size() && total_price == obj.total_price;
    }

    public Package(String owner_email, int package_id, LocalDate startDate, LocalDate endDate) {
        this.products = new ArrayList<Product>();
        this.owner_email = owner_email;
        this.package_id = package_id;
        this.total_price = 0;
        this.cancellation_policy = "No Policy";
        address = new Address("","","");
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Package(String owner_email, int package_id, String startDate, String endDate) {
        this.products = new ArrayList<Product>();
        this.owner_email = owner_email;
        this.package_id = package_id;
        this.total_price = 0;
        this.cancellation_policy = "No Policy";
        address = new Address("","","");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.startDate = LocalDate.parse(startDate, formatter);
        this.endDate = LocalDate.parse(endDate, formatter);;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Product> getProducts() {
        return products;
    }


    public String getOwner_email() {
        return owner_email;
    }


    public int getPackage_id() {
        return package_id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public String getCancellation_policy() {
        return cancellation_policy;
    }

    public void setCancellation_policy(String cancellation_policy) {
        this.cancellation_policy = cancellation_policy;
    }

    public void addProduct(Product product) {
        total_price += product.price;
        this.products.add(product);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStartDateString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return this.startDate.format(formatter);
    }

    public String getEndDateString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return this.endDate.format(formatter);
    }
}
