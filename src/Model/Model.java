package Model;

import App.*;
import App.Package;
import DB.SqliteDB;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Model {

    private final SqliteDB db;

    public Model() {
        this.db = new SqliteDB();
        db.connectToDB("myDB.db",false);
    }

    public void addUser(User user) {
        db.addUser(user);
    }

    public Boolean isUserExists(User u) {
        return db.isUserExists(u);
    }

    public void addPackage(Package aPackage) {
        db.addPackage(aPackage);
    }

    public List<Package> getUserPackages(String email) {
        return db.getPackageByOwnerEmail(email);
    }

    public User loadUser(User u) {
        return db.getUserByEmail(u.email);
    }

    public void deleteProduct(String owner_email, int packageID, int productID) {
        db.deleteProduct(new Product(owner_email, productID, packageID, 0,"none"));
        Package aPackage = getPackage(owner_email, packageID);
        if(aPackage.getProducts().size() == 0)
            deletePackage(aPackage);
    }

    public Package getPackage(String ownerEmail, int packageID) {
        return db.getPackageByOwnerIdAndPackageId(ownerEmail, packageID);
    }

    public void deletePackage(Package pack) {
        for (Product p : pack.getProducts()) {
            db.deleteProduct(p);
        }
        db.deletePackage(pack);
    }

    public List<String> getAllCategories() {
        return db.getAllCategories();
    }

    public List<String> getAllPackageCancelationPoliciy() {
        return db.getAllCancellationPolicy();
    }

    public List<Package> getPackagesBy(LocalDate startDateValue, LocalDate endDateValue) {
        List<Package> packageList = db.getPackagesBy(startDateValue, endDateValue);
        return packageList;
    }

    public List<Package> getPackagesByCategory(String category) {
        List<Package> packageList = db.getPackagesByCategory(category);
        return packageList;
    }

    public void addOrder(Order o) {
        try {
            db.addOrder(o);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Package> getUnOrderedUserPackages(String email) {
        return db.getUnOrderedPackageByOwnerEmail(email);
    }

    public List<Package> getPackagesByAddress(Address address) {
        List<Package> packageList = db.getPackageByAddress(address);
        return packageList;
    }
}
