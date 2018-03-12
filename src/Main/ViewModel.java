/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import App.Order;
import App.Address;
import App.Package;
import App.Product;
import App.User;
import Model.Model;
import View.AddPackageView.AddPackageController;
import View.AddProductView.AddProductController;
import View.PackageDescriptionView.PackageDescriptionView;
import View.SearchView.SearchViewController;
import View.SignInScreenView.SignInController;
import View.SignUpScreenView.SignUpController;
import View.UserViewScreen.ProductEntry;
import View.UserViewScreen.UserViewController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author proxc
 */
public class ViewModel extends Application
{

    //init xy offsets
    private double xOffset = 0;
    private double yOffset = 0;
    private Scene signInScene;
    private Scene signUpScene;
    private Model model;
    private Stage stage;
    private Scene userViewScene;
    private Scene addPackageScene;
    private AddPackageController addPackageController;
    private UserViewController userViewController;
    private AddProductController addProductLoaderController;
    private Scene addProductScene;
    private User user;
    private Package aPackage;
    private Scene PackageDescriptionView;
    private PackageDescriptionView packageDescriptionViewController;
    private Scene searchView;
    private SearchViewController searchViewController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader signUpLoader = new FXMLLoader(getClass().getResource("../View/SignUpScreenView/SignUpScreen.fxml"));
        Parent signUpRoot = (Parent) signUpLoader.load();

        FXMLLoader signInLoader = new FXMLLoader(getClass().getResource("../View/SignInScreenView/SignInScreen.fxml"));
        Parent signInRoot = (Parent) signInLoader.load();

        FXMLLoader userViewLoader = new FXMLLoader(getClass().getResource("../View/UserViewScreen/UserView.fxml"));
        Parent userViewRoot = (Parent) userViewLoader.load();

        FXMLLoader addPackageLoader = new FXMLLoader(getClass().getResource("../View/AddPackageView/AddPackage.fxml"));
        Parent addPackageRoot = (Parent) addPackageLoader.load();

        FXMLLoader addProductLoader = new FXMLLoader(getClass().getResource("../View/AddProductView/AddProduct.fxml"));
        Parent addProductRoot = (Parent) addProductLoader.load();

        FXMLLoader PackageDescriptionViewLoader = new FXMLLoader(getClass().getResource("../View/PackageDescriptionView/PackageDescriptionView.fxml"));
        Parent packageDescriptionRoot = (Parent) PackageDescriptionViewLoader.load();

        FXMLLoader searchViewLoader = new FXMLLoader(getClass().getResource("../View/SearchView/SearchScreen.fxml"));
        Parent searchViewRoot = (Parent) searchViewLoader.load();

        this.stage = stage;
        this.stage.initStyle(StageStyle.UNDECORATED);

        //set mouse pressed
        setDraggable(stage, signUpRoot);
        setDraggable(stage, signInRoot);
        setDraggable(stage, userViewRoot);
        setDraggable(stage, addPackageRoot);
        setDraggable(stage, addProductRoot);
        setDraggable(stage, packageDescriptionRoot);
        setDraggable(stage, searchViewRoot);

        signUpScene = new Scene(signUpRoot);
        signInScene = new Scene(signInRoot);
        userViewScene = new Scene(userViewRoot);
        addPackageScene = new Scene(addPackageRoot);
        addProductScene = new Scene(addProductRoot);
        PackageDescriptionView = new Scene(packageDescriptionRoot);
        searchView = new Scene(searchViewRoot);

        Model model = new Model();
        setModel(model);
        SignUpController controller = signUpLoader.getController();
        controller.setViewModel(this);

        SignInController signInController = signInLoader.getController();
        signInController.setViewModel(this);

        userViewController = userViewLoader.getController();
        userViewController.setViewModel(this);

        addPackageController = addPackageLoader.getController();
        addPackageController.setViewModel(this);

        addProductLoaderController = addProductLoader.getController();
        addProductLoaderController.setViewModel(this);

        packageDescriptionViewController = PackageDescriptionViewLoader.getController();
        packageDescriptionViewController.setViewModel(this);

        searchViewController = searchViewLoader.getController();
        searchViewController.setViewModel(this);

//        stage.setScene(signInScene);
        stage.setScene(searchView);
        stage.show();
    }

    public void setDraggable(Stage stage, Parent parent) {
        parent.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        //set mouse drag
        parent.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void goToSignUp() {
        stage.setScene(signUpScene);
    }

    public void addUser(User u) {
        model.addUser(u);
    }

    public void goToSignIn() {
        user = null;
        stage.setScene(signInScene);
    }

    public Boolean isUserExists(User u) {
        return model.isUserExists(u);
    }

    public void goToUserView() {
        userViewController.loadUserData(user);
        userViewController.setUser(user);
        stage.setScene(userViewScene);

    }

    public void goToAddPackage() {
        stage.setScene(addPackageScene);
    }

    public void goToAddProduct() {
        stage.setScene(addProductScene);
    }

    public void addPackage(Package aPackage) {
        model.addPackage(aPackage);
    }

    public void createNewPackage(Address address, String cancellation_policy, LocalDate startDate, LocalDate endDate) {
        aPackage = new Package(user.email, 0, startDate, endDate);
        aPackage.setAddress(address);
        aPackage.setCancellation_policy(cancellation_policy);
    }

    public void addProductToPackage(int price, String categoryText, String description) {
        Product product = new Product(user.email, 0, 0, price, categoryText);
        product.description = description;
        aPackage.addProduct(product);
    }

    public void savePackage() {
        if (aPackage != null && aPackage.getProducts().size() > 0) {
            model.addPackage(aPackage);
            userViewController.addToTable(aPackage);
        }
        aPackage = null;
    }

    public void loadUser(User u) {
        user = u;
    }

    public List<Package> getPackagesOfUser() {
        return model.getUserPackages(user.email);
    }

    public Boolean loadUser(String email, String pass) {
        User u = new User(email, pass);
        if (model.isUserExists(u)) {
            user = model.loadUser(u);
            return true;
        }
        return false;

    }

    public void discartPackage() {
        aPackage = null;
    }

    public void deleteProduct(String owner_emailText, int packageID, int productID) {
        if(aPackage != null) {
            List<Product> products = new ArrayList<>(aPackage.getProducts());
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                if (p.ownerEmail.equals(owner_emailText) && p.packageID == packageID && p.productID == productID) {
                    aPackage.getProducts().remove(i);
                    break;
                }
            }
        }
        model.deleteProduct(owner_emailText, packageID, productID);
        userViewController.deleteProductFromTable(owner_emailText, packageID, productID);
    }

    public void updateProduct(Product prod, Address address) {
        Package pack = model.getPackage(prod.ownerEmail, prod.packageID);
        model.deletePackage(pack);
        pack.setAddress(address);
        for (Product p : pack.getProducts()) {
            if(p.productID == prod.productID)
            {
                p.price = prod.price;
                p.category = prod.category;
                p.description = prod.description;
            }
        }
        model.addPackage(pack);
        userViewController.deletePackageFromTable(pack);
        userViewController.addToTable(pack);
    }

    public List<String> getAllCategories() {
        return model.getAllCategories();
    }

    public List<String> getAllPackageCancellationPolicy() {
        return model.getAllPackageCancelationPoliciy();
    }

    public void searchPackagesBy(List<Package> packagesList) {
        packageDescriptionViewController.addPackagesToTable(packagesList);
        if(user != null)
            packageDescriptionViewController.setUserLoggedIn();
        else
            packageDescriptionViewController.setUserLoggedOut();
        stage.setScene(PackageDescriptionView);
    }

    public List<Package> searchPackagesByDate(LocalDate startDateValue, LocalDate endDateValue) {
        return model.getPackagesBy(startDateValue, endDateValue);
    }

    public List<Package> getPackagesByCategory(String category) {
        return model.getPackagesByCategory(category);
    }

    public void goToSearchView() {
        stage.setScene(searchView);
    }

    public void goToPackageDescriptionView() {
        stage.setScene(PackageDescriptionView);
    }

    public void loguotUser() {
        user = null;
    }

    public void addRentOrder(ProductEntry clickedProductRow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.parse(clickedProductRow.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(clickedProductRow.getEndDate(), formatter);
        Order o = new Order(clickedProductRow.getOwnerEmail(), user.email,startDate,endDate,clickedProductRow.getPrice(),clickedProductRow.getPackageID(),"Rented");
        model.addOrder(o);
    }

    public void addTradeOrder(ProductEntry clickedProductRow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.parse(clickedProductRow.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(clickedProductRow.getEndDate(), formatter);
        Order o = new Order(clickedProductRow.getOwnerEmail(), user.email,startDate,endDate,clickedProductRow.getPrice(),clickedProductRow.getPackageID(),"Traded");
        model.addOrder(o);
    }

    public List<Package> getUnOrderdPackagesOfUser() {
        return model.getUnOrderedUserPackages(user.email);
    }

    public void popAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public User getUser() {
        return user;
    }

    public List<Package> getPackagesByAddress(String city, String neighborhood, String street) {
        return model.getPackagesByAddress(new Address(city, neighborhood,street));
    }

    public void sendEmail(String Email) throws MessagingException {
        String host = "smtp.gmail.com";
        String user = "everything4rent4@gmail.com";
        String pass = "nituz123";
        String to = Email;
        String from = "everything4rent4@gmail.com";
        String subject = "Welcome to Everything4Rent";
        String message = "You are now a member of Everything4Rent system";
        boolean sessionDebug = false;


        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.required", "true");

        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(sessionDebug);
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = {new InternetAddress(to)};
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject); msg.setSentDate(new Date());
        msg.setText(message);

        Transport transport=mailSession.getTransport("smtp");
        transport.connect(host, user, pass);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        System.out.println("message send successfully");

    }
}
