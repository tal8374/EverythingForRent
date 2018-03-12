package App;
import java.time.LocalDate;
import java.util.Date;

public class Order {

    private String tenant_email;
    private String renter_email;
    private LocalDate start_date;
    private LocalDate end_date;
    private int total_price;
    private int package_id;
    private String status;

    public Order(String tenant_email, String renter_email, LocalDate start_date, LocalDate end_date, int total_price, int package_id, String status) {
        this.tenant_email = tenant_email;
        this.renter_email = renter_email;
        this.start_date = start_date;

        this.end_date = end_date;
        this.total_price = total_price;
        this.package_id = package_id;
        this.status = status;
    }

    public String getTenant_email() {
        return tenant_email;
    }

    public void setTenant_email(String tenant_email) {
        this.tenant_email = tenant_email;
    }

    public String getRenter_email() {
        return renter_email;
    }

    public void setRenter_email(String renter_email) {
        this.renter_email = renter_email;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
