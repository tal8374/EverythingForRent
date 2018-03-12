package DB;

import App.*;
import App.Package;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class SqliteDB {
    private Connection dbConnection;

    public void connectToDB(String path, Boolean deleteDB) {
        try {
            Class.forName("org.sqlite.JDBC");
            if(deleteDB)
                new File(path).delete();
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + path);

            createProductsTable();
            createPackageTable();
            createOrdersTable();
            createWorkDaysTable();
            createUsersTable();
//            createRenterTable();
//            createTenantTable();
            createCategoryTable();
            createCancellationPolicyTable();
            createSearchTable();

            System.out.println("db init");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    //**************** Create Tables **************************


    private void createTenantTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Tenant (\n" +
                "user_email varchar(255),\n" +
                "package_id int,\n" +
                "CONSTRAINT PK_Tenant PRIMARY KEY (user_email,package_id));";
        execute(sql);
    }

    private void createSearchTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Search (\n" +
                "owner_email varchar(255), \n" +
                "package_id int,\n" +
                "numberOfSearches int,\n" +
                "CONSTRAINT PK_Search PRIMARY KEY (package_id, owner_email));";
        execute(sql);

    }

    private void createRenterTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Renter (\n" +
                "user_email varchar(255),\n" +
                "colPackageId int,\n" +
                "CONSTRAINT PK_Renter PRIMARY KEY (user_email,colPackageId));");
    }

    private void createWorkDaysTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Work_Days (\n" +
                "colPackageId int ,\n" +
                "day varchar(255),\n" +
                "CONSTRAINT PK_Work_Days PRIMARY KEY (colPackageId,day));");
    }

    private void createOrdersTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Orders (\n" +
                "tenant_email varchar(255) ,\n" +
                "renter_email varchar(255) ,\n" +
                "start_date DATETIME,\n" +
                "end_date DATETIME,\n" +
                "total_price int,\n" +
                "package_id int,\n" +
                "status varchar(255),\n" +
                "CONSTRAINT PK_Orders PRIMARY KEY (tenant_email,renter_email,start_date, package_id));");
    }


    private void createUsersTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Users (\n" +
                "first_name varchar(255),\n" +
                "last_name varchar(255),\n" +
                "password varchar(255),\n" +
                "email varchar(255) PRIMARY KEY \n" +
                ");");
    }

    private void createPackageTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Packages (\n" +
                "owner_email varchar(255) ,\n" +
                "package_id int ,\n" +
                "total_price int,\n" +
                "cancellation_policy varchar(30),\n" +
                "city varchar(255) ,\n" + "neighborhood varchar(255) ,\n" + "street varchar(255) ,\n" +
                "start_date DATETIME ,\n" +
                "end_date DATETIME ,\n" +
                "CONSTRAINT PK_Packages PRIMARY KEY (owner_email,package_id), \n" +
                "CONSTRAINT FK_Packages FOREIGN KEY (owner_email) REFERENCES Users(email)) ;"
        );
    }

    private void createProductsTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Products (\n" +
                "owner_email varchar(255) ,\n" +
                "product_id int ,\n" +
                "package_id int,\n" +
                "price int,\n" +
                "category varchar(255),\n" +
                "description varchar(255),\n" +
                "CONSTRAINT PK_Products PRIMARY KEY (owner_email,product_id,package_id), \n" +
                "CONSTRAINT FK_Products1 FOREIGN KEY (owner_email) REFERENCES Users(email), \n" +
                "CONSTRAINT FK_Products2 FOREIGN KEY (package_id) REFERENCES Packages(package_id)) \n" +
                ";");
    }

    private void createCategoryTable()throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Category (\n" +
                "category varchar(255),\n" +
                "CONSTRAINT PK_Category PRIMARY KEY (category) \n" +
                ");");
        addCategory("Real estate");
        addCategory("Second hand");
        addCategory("Vehicle");
        addCategory("Pets");
    }

    private void createCancellationPolicyTable() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS CancellationPolicy (\n" +
                "cancellation_policy varchar(255),\n" +
                "CONSTRAINT PK_CancellationPolicy PRIMARY KEY (cancellation_policy)" +
                ");";
        execute(sql);
        addCancellationPolicy("Safe");
        addCancellationPolicy("Conservative");
        addCancellationPolicy("First come first served");
    }

    //*******************Add *****************************************

    public void addSearch(String owner_email, int package_id) throws SQLException {
        String sql = "IF EXISTS(SELECT * FROM Search as s WHERE s.owner_email = '"+owner_email+"' AND s.package_id = "+package_id+" )\n" +
                     "   UPDATE Search SET s.numberOfSearches = s.numberOfSearches + 1 \n" +
                     "ELSE\n" +
                     "   insert into Search values('"+owner_email+"', "+package_id+", 0);";

        if(isSearchExsits(owner_email,package_id))
        {
            sql =  "UPDATE Search \n" +
                    "SET numberOfSearches = numberOfSearches + 1\n" +
                    "WHERE owner_email = '"+owner_email+"' AND package_id = "+package_id+"";
        }
        else {
            sql =  "INSERT INTO Search values('"+owner_email+"', "+package_id+", 1);";
        }
        execute(sql);
    }

    private boolean isSearchExsits(String owner_email, int package_id) {
        String query = "SELECT * FROM Search as s WHERE s.owner_email = '"+owner_email+"'  AND s.package_id = "+package_id+" ;";
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            String c = resSet.getString("owner_email");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void addCancellationPolicy(String policy) {
        if(!isCancellationPolicyExists(policy)) {
            try {
                String query = "INSERT INTO CancellationPolicy \n" +
                        "VALUES ('" + policy + "') " +
                        ";";
                execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCategory(String category){
        if(!isCategoryExists(category)) {
            try {
                String query = "INSERT INTO Category \n" +
                        "VALUES ('" + category + "') " +
                        ";";
                execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addUser(String firstName, String lastName, String password, String email) {
        try {
            String query = "INSERT INTO Users \n" +
                    "VALUES ('" + firstName + "','" + lastName + "','" + password + "','" + email + "');";
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addUser(User user) {
        addUser(user.first_name, user.last_name, user.password, user.email);
    }

    public void addProduct(Product p) {
        try {
            Statement st = dbConnection.createStatement();
            p.productID = getNextProductIdForPackage(p.ownerEmail,p.packageID);
            String query = "INSERT INTO Products \n" +
                    String.format("VALUES ('%s',%d, %d, %d, '%s', '%s');", p.ownerEmail, p.productID, p.packageID, p.price, p.category, p.description);
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPackage(Package pack) {
        try {
            String owner_id = pack.getOwner_email();
            int package_id = getNextPackageIdForUser(owner_id);
            int total_price = pack.getTotal_price();
            String cancellation_policy = pack.getCancellation_policy();

            String startDate = pack.getStartDateString();
            String endDate = pack.getEndDateString();

            String query = String.format("INSERT INTO Packages " +
                            "VALUES('%s', %d, %d, '%s', '%s', '%s', '%s', '%s', '%s')", owner_id, package_id, total_price,
                    cancellation_policy, pack.getAddress().getCity(), pack.getAddress().getNeighborhood(), pack.getAddress().getStreet(), startDate, endDate);
            execute(query);
            for (Product p : pack.getProducts()) {
                p.packageID = package_id;
                p.ownerEmail = owner_id;
                addProduct(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //*************** Delete ******************************

    public void deleteUser(User user) {
        try {
            execute("DELETE FROM Users WHERE Users.email = '" + user.email + "' ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deletePackage(Package pack) {
        try {
            execute("DELETE FROM Packages WHERE Packages.owner_email = '" + pack.getOwner_email() +
                    "' AND Packages.package_id = " + pack.getPackage_id() + " ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(Product p) {
        try {
            execute("DELETE FROM Products WHERE Products.product_id = " + p.productID +
                    " AND Products.package_id = " + p.packageID + " AND Products.owner_email = " +
                    "'" + p.ownerEmail + "' ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUsers() {
        try {
            execute("DELETE FROM Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProducts() {
        try {
            execute("DELETE FROM Products");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePackages() {
        try {
            execute("DELETE FROM Packages");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ******************** Get ********************************
    public List<String> getAllCategories(){
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Category ;");
            List<String> categories = new ArrayList<>();
            while (resSet.next()) {
                categories.add(resSet.getString("category"));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Package> getPackagesByCategory(String category) {
        try {
//            Statement st = dbConnection.createStatement();
            Statement st = dbConnection.createStatement();
            String sql = "SELECT DISTINCT * FROM Packages " +
                    "INNER JOIN Products ON Packages.package_id=Products.package_id AND Packages.owner_email=Products.owner_email " +
                    "WHERE Products.category=" + "'" +  category + "'" +
                    "AND NOT EXISTS ( " +
                    "                    SELECT * FROM Orders as o WHERE " +
                    "                    o.package_id = Packages.package_id AND o.tenant_email = Packages.owner_email " +
                    "                    );";
            ResultSet resSet = st.executeQuery(sql);
            List<Package> packages = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            while (resSet.next()) {
                Package p = getPackageFromRow(resSet);
                String o = p.getOwner_email() + p.getPackage_id();
                if(!seen.contains(o)) {
                    packages.add(getPackageByOwnerIdAndPackageId(p.getOwner_email(), p.getPackage_id()));
                    seen.add(o);
                }

            }
            return packages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Package> getPackagesBy(LocalDate startDateValue, LocalDate endDateValue) {
        try {
            Statement st = dbConnection.createStatement();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String sql = "SELECT * FROM Packages as p " +
                    "WHERE p.start_date <= '" + startDateValue.format(formatter) + "' " +
                    "AND p.end_date >= '" + endDateValue.format(formatter) + "' " +
                    "AND NOT EXISTS (" +
                    "SELECT * FROM Orders as o WHERE " +
                    "o.package_id = p.package_id AND o.tenant_email = p.owner_email" +
                    ");";
            ResultSet resSet = st.executeQuery(sql);
            List<Package> packages = new ArrayList<>();
            while (resSet.next()) {
                Package p = getPackageFromRow(resSet);
                packages.add(getPackageByOwnerIdAndPackageId(p.getOwner_email(), p.getPackage_id()));
            }
            return packages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllSearches(){
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Search ;");
            List<String> searches = new ArrayList<>();
            while (resSet.next()) {
                searches.add(resSet.getString("colPackageId"));
            }
            return searches;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllCancellationPolicy(){
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM CancellationPolicy ;");
            List<String> cancellation_policys = new ArrayList<>();
            while (resSet.next()) {
                cancellation_policys.add(resSet.getString("cancellation_policy"));
            }
            return cancellation_policys;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Product getProductByEmailProductIdAndPackageId(String ownerEmail, int pId, int packageId) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Products as P " +
                    "WHERE P.product_id = " + pId + " AND P.package_id = " + packageId + " " +
                    "AND P.owner_email = '"+ ownerEmail +"' ;");
            return getProductFromRow(resSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Product getProductFromRow(ResultSet resSet) throws SQLException {
        String owner_email = resSet.getString("owner_email");
        int product_id = resSet.getInt("product_id");
        int package_id = resSet.getInt("package_id");
        int price = resSet.getInt("price");
        String category = resSet.getString("category");
        Product product = new Product(owner_email, product_id, package_id, price, category);
        product.description = resSet.getString("description");
        return product;
    }

    public User getUserByEmail(String email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT * \n" +
                    "FROM Users \n" +
                    "WHERE Users.email = '" + email + "' ;");
            return getUserFromRow(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User("s", "l", "p", "e");
    }

    private User getUserFromRow(ResultSet resultSet) throws SQLException {
        String userFirstName = resultSet.getString("first_name");
        String userLastName = resultSet.getString("last_name");
        String password = resultSet.getString("password");
        String email1 = resultSet.getString("email");
        return new User(userFirstName, userLastName, password, email1);
    }

    public Package getPackageByOwnerIdAndPackageId(String ownerEmail, int packageId) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Packages as p " +
                    "WHERE p.owner_email = '" + ownerEmail + "' AND p.package_id = " + packageId + ";");
            Package p = getPackageFromRow(resSet);
            resSet = st.executeQuery("SELECT * FROM Products as p " +
                    "WHERE p.package_id = " + packageId + " AND p.owner_email = '"+ownerEmail + "' ;");
            while (resSet.next()) {
                p.addProduct(getProductFromRow(resSet));
            }
            return p;
        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println(String.format("There is no package %d of %s", packageId, ownerEmail));
        }
        return null;
    }

    public List<Package> getPackageByOwnerEmail(String email) {
        String query = String.format("SELECT p.package_id FROM Packages as p WHERE p.owner_email = '%s' ;",email);
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            ArrayList<Integer> packages_ids = new ArrayList<Integer>();
            while (resSet.next()) {
                packages_ids.add(resSet.getInt("package_id"));
            }
            ArrayList<Package> packages = new ArrayList<Package>();
            for (int pack_id : packages_ids) {
                packages.add(getPackageByOwnerIdAndPackageId(email, pack_id));
            }
            return packages;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Package getPackageFromRow(ResultSet resSet) throws SQLException {
        String owner_email = resSet.getString("owner_email");
        int package_id = resSet.getInt("package_id");
        String startDateString = resSet.getString("start_date");
        String endDateString = resSet.getString("end_date");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);;
        Package p = new Package(owner_email, package_id, startDate, endDate);
        String cancellation_policy = resSet.getString("cancellation_policy");
        p.setCancellation_policy(cancellation_policy);
//        String address = resSet.getString("address");
//        p.setAddress(address);
        String city = resSet.getString("city");
        String neighborhood = resSet.getString("neighborhood");
        String street = resSet.getString("street");
        p.setAddress(new Address(city,neighborhood,street));
        return p;
    }

    public int getNextPackageIdForUser(String email) {
        String query = String.format("SELECT MAX(p.package_id) FROM Packages as p WHERE p.owner_email = '%s' ;",email);
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            return resSet.getInt(1) + 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int getNextProductIdForPackage(String email, int packageId) {
        String query = String.format("SELECT MAX(p.package_id) FROM Products as p WHERE p.owner_email = '%s'" +
                " AND p.package_id = %d ;",email, packageId);
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            return resSet.getInt(1) + 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

//    ******************** Check if Exists ***********************************

    public Boolean isUserExists(User user) {
        String query = "SELECT * FROM Users as u WHERE u.email = '" + user.email + "' ;";
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            User resUser = getUserFromRow(resSet);
            return resUser.password.equals(user.password);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isProductExists(Product p) {
        String query = "SELECT * FROM Products as p WHERE p.product_id = " + p.productID +
                " AND p.package_id = " + p.packageID + " ;";
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            Product resProduct = getProductFromRow(resSet);
            return p.price == resProduct.price && p.category.equals(resProduct.category);
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isCancellationPolicyExists(String policy) {
        String query = "SELECT * FROM CancellationPolicy WHERE cancellation_policy = '" + policy + "' ;";
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            String c = resSet.getString("cancellation_policy");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isCategoryExists(String category) {
        String query = "SELECT * FROM Category WHERE category = '" + category + "' ;";
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            String c = resSet.getString("category");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isPackageExists(Package pack) {
        String query = "SELECT * FROM Packages as p WHERE p.package_id = " + pack.getPackage_id() +
                " AND p.owner_email = '" + pack.getOwner_email() + "' ;";
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            Package aPackage = getPackageFromRow(resSet);
            return pack.getOwner_email().equals(aPackage.getOwner_email()) && pack.getPackage_id() == aPackage.getPackage_id();
        } catch (SQLException e) {
            return false;
        }
    }

    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void execute(String sql) throws SQLException {
        Statement st = dbConnection.createStatement();
        st.execute(sql);
    }

    public void addOrder(Order o) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String start_date = o.getStart_date().format(formatter);
        String end_date = o.getEnd_date().format(formatter);
        String sql = String.format("INSERT INTO Orders" +
                " VALUES('%s', '%s', '%s', '%s', %d, %d, '%s');",
                o.getTenant_email(), o.getRenter_email(), o.getStart_date(), o.getEnd_date(), o.getTotal_price(),
                o.getPackage_id(), o.getStatus());
        execute(sql);
    }

    public List<Package> getUnOrderedPackageByOwnerEmail(String email) {
        String sql = "SELECT p.package_id FROM Packages as p WHERE p.owner_email = '%s' " +
                "AND NOT EXISTS (" +
                "                SELECT * FROM Orders as o WHERE " +
                "                o.package_id = p.package_id AND o.tenant_email = p.owner_email" +
                "                );";
        String query = String.format(sql,email);
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            ArrayList<Integer> packages_ids = new ArrayList<Integer>();
            while (resSet.next()) {
                packages_ids.add(resSet.getInt("package_id"));
            }
            ArrayList<Package> packages = new ArrayList<Package>();
            for (int pack_id : packages_ids) {
                packages.add(getPackageByOwnerIdAndPackageId(email, pack_id));
            }
            return packages;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Package> getPackageByAddress(Address address) {
        String city = address.getCity();
        String neighborhood = address.getNeighborhood();
        String street = address.getStreet();


        String query = "SELECT p.package_id FROM Packages as p WHERE " +
                "p.city = '" + city + "' " +
                "AND p.neighborhood = '" + neighborhood +  "' " +
                "AND p.street = '" + street + "' " +
                "AND NOT EXISTS (" +
                        "SELECT * FROM Orders as o WHERE " +
                        "o.package_id = p.package_id AND o.tenant_email = p.owner_email" +
                ");";
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery(query);
            ArrayList<Integer> packages_ids = new ArrayList<Integer>();
            while (resSet.next()) {
                packages_ids.add(resSet.getInt("package_id"));
            }
            ArrayList<Package> packages = new ArrayList<Package>();
            for (int pack_id : packages_ids) {
                packages.add(getPackageByPackageId(pack_id));
            }

            return packages;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Package getPackageByPackageId(int pack_id){
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Packages as p WHERE  p.package_id = " + pack_id + ";");
            Package p = getPackageFromRow(resSet);
            return p;
        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println(String.format("There is no package  %d", pack_id));
        }
        return null;
    }

}


