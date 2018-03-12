package DB;

import App.*;
import App.Package;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SqliteDBTests {

    private SqliteDB db;

    @Before
    public void setUp() throws Exception {
        db = new SqliteDB();
        db.connectToDB("test1.db", true);
    }

    @Test
    public void testAddUser() {
        db.addUser(new User("msdaor", "r", "123", "goole@fgfg.com"));
        User u = db.getUserByEmail("goole@fgfg.com");
        Assert.assertEquals(u.email, "goole@fgfg.com");
        db.close();
    }

    @Test
    public void testAddProduct() {
        Product p = new Product("email", 1, 24, 3000, "realastate");
        db.addProduct(p);
        Product resP = db.getProductByEmailProductIdAndPackageId("email", 1, 24);
        Assert.assertEquals(1, resP.productID);
        Assert.assertEquals(24, resP.packageID);
        Assert.assertEquals(3000, resP.price);
        db.close();
    }

    @Test
    public void testAddPackage() {
        String owner_email = "email@ggg.com";
        db.addUser(new User("msdaor", "r", "123", owner_email));
        Product p1 = new Product(owner_email, 1, 34, 3000, "realastate");
        Product p2 = new Product(owner_email, 4, 34, 1000, "realastate");
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Package pack = new Package(owner_email, 0, startDate, endDate);
        pack.setCancellation_policy("safe");
        pack.setAddress(new Address("Tel-Aviv", "haTikva", "haShalom"));
        pack.addProduct(p1);
        pack.addProduct(p2);
        db.addPackage(pack);
        Package resPackage = db.getPackageByOwnerIdAndPackageId(owner_email, 1);
        Assert.assertEquals(resPackage.getTotal_price(), 4000);
        Assert.assertEquals(resPackage.getOwner_email(), owner_email);
        Assert.assertEquals(resPackage.getPackage_id(), 1);
        db.close();
    }

    @Test
    public void testUserExists() {
        User user = new User("msdaor", "r", "123", "goole@fgfg.com");
        User user2 = new User("msdaor", "r", "1263", "goole@fgfg.com");
        User user3 = new User("msdfsdr", "sdf", "123", "godfdle@fgfg.com");
        db.addUser(user);
        Assert.assertTrue(db.isUserExists(user));
        Assert.assertFalse(db.isUserExists(user2));
        Assert.assertFalse(db.isUserExists(user3));
        db.close();
    }

    @Test
    public void testDeleteUser() {
        User user = new User("g", "r", "123", "goole@fgfg.com");
        db.addUser(user);
        Assert.assertTrue(db.isUserExists(user));
        db.deleteUser(user);
        Assert.assertFalse(db.isUserExists(user));
        db.close();
    }

    @Test
    public void testDeleteProduct() {
        Product p = new Product("emails", 4, 14, 200, "realastate");
        db.addProduct(p);
        Assert.assertTrue(db.isProductExists(p));
        db.deleteProduct(p);
        Assert.assertFalse(db.isProductExists(p));
        db.close();
    }

    @Test
    public void testDeletePackage() {
        String ownerEmail = "email@ggg.com";
        User user = new User("g", "r", "123", ownerEmail);
        db.addUser(user);
        Product p1 = new Product(ownerEmail, 1, 7, 300, "realastate");
        Product p2 = new Product(ownerEmail, 2, 7, 100, "realastate");
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Package pack = new Package(ownerEmail, 7, startDate, endDate);
        pack.setCancellation_policy("safe");
        pack.setAddress(new Address("Tel-Aviv", "haTikva", "haShalom"));
        pack.addProduct(p1);
        pack.addProduct(p2);
        db.addPackage(pack);
        Assert.assertTrue(db.getPackageByOwnerEmail(ownerEmail).size() == 1);
        db.deletePackage(pack);
        Assert.assertFalse(db.getPackageByOwnerEmail(ownerEmail).size() == 0);
        db.close();
    }

    @Test
    public void testGetPackagesByOwnerEmail() {
        String email = "d@fgfg.com";
        User user = new User("d", "r", "123", email);
        int package_id1 = 9;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Package pack1 = new Package(email, package_id1, startDate, endDate);
        Product p1 = new Product(email, 1, package_id1, 300, "realastate");
        Product p2 = new Product(email, 2, package_id1, 100, "realastate");
        pack1.addProduct(p1);
        pack1.addProduct(p2);
        int package_id2 = 4;
        Package pack2 = new Package(email, package_id2, startDate, endDate);
        Product p3 = new Product(email, 1, package_id2, 600, "realastate");
        Product p4 = new Product(email, 2, package_id2, 100, "realastate");
        pack2.addProduct(p3);
        pack2.addProduct(p4);
        db.addUser(user);
        db.addPackage(pack1);
        db.addPackage(pack2);
        List<Package> packageList = db.getPackageByOwnerEmail(email);
        Assert.assertTrue(pack1.getOwner_email().equals(packageList.get(0).getOwner_email()));
        Assert.assertTrue(1 == packageList.get(0).getPackage_id());
        Assert.assertTrue(2 == packageList.get(1).getPackage_id());
        Assert.assertTrue(1 == packageList.get(0).getProducts().get(0).productID);
        Assert.assertTrue(2 == packageList.get(0).getProducts().get(1).productID);
        db.close();
    }

    @Test
    public void testGetNextPackageId() {
        String email = "e@fgfg.com";
        User user = new User("d", "r", "123", email);
        db.addUser(user);
        int nextPackageId = db.getNextPackageIdForUser(email);
        Assert.assertEquals(nextPackageId, 1);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Package pack1 = new Package(email, nextPackageId, startDate, endDate);
        Product p1 = new Product(email, 1, nextPackageId, 300, "realastate");
        Product p2 = new Product(email, 2, nextPackageId, 100, "realastate");
        pack1.addProduct(p1);
        pack1.addProduct(p2);
        db.addPackage(pack1);
        nextPackageId = db.getNextPackageIdForUser(email);
        Assert.assertEquals(nextPackageId, 2);
        db.close();
    }

    @Test
    public void testGetNextProductIdForPackage() {
        String email = "e@fgfg.com";
        User user = new User("d", "r", "123", email);
        db.addUser(user);
        int nextPackageId = db.getNextPackageIdForUser(email);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Package pack1 = new Package(email, nextPackageId, startDate, endDate);
        int prod_id = db.getNextProductIdForPackage(email, nextPackageId);
        Product p1 = new Product(email, prod_id, nextPackageId, 300, "realastate");
        pack1.addProduct(p1);
        db.addPackage(pack1);
        Assert.assertEquals(1, prod_id);
        int nextProductId = db.getNextProductIdForPackage(email, nextPackageId);
        Assert.assertEquals(2, nextProductId);
        db.close();
    }

    @Test
    public void testGetAllCancelationPolicies() {
        List<String> policys = db.getAllCancellationPolicy();
        String[] expected = {"Safe", "Conservative", "First come first served"};
        Assert.assertArrayEquals(policys.toArray(), expected);
        db.close();
    }

    @Test
    public void testGetAllCategories() {
        List<String> categories = db.getAllCategories();
        String[] expected = {"Real estate", "Second hand", "Vehicle", "Pets"};
        Assert.assertArrayEquals(categories.toArray(), expected);
        db.close();
    }

    @Test
    public void testInsertOrder() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.parse("2016/06/02", formatter);
        LocalDate endDate = LocalDate.parse("2016/07/02", formatter);
        Order o = new Order("tenant","renter",startDate,endDate,1000,45,"Rented");
        db.addOrder(o);
    }

    @Test
    public void testAddSearch() throws SQLException {
        db.addSearch("maor", 3);
        db.addSearch("maor", 3);
        db.addSearch("maor", 3);
    }

}
