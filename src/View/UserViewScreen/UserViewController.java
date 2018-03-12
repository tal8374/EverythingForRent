package View.UserViewScreen;

import App.Product;
import App.User;
import App.Package;
import View.ProductDescriptionView.ProductViewController;
import Main.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserViewController implements Initializable
{
    public TableView<ProductEntry> product_table;
    public TableColumn<ProductEntry, Integer> colPrice;
    public TableColumn<ProductEntry, Integer> colPackageId;
    public TableColumn<ProductEntry, Integer> colProductId;
    public TableColumn<ProductEntry, Integer> colCategory;
    public TableColumn<ProductEntry, Integer> colEnd;
    public TableColumn<ProductEntry, Integer> colStart;
    public TableColumn<ProductEntry, Integer> colDescription;
    private ViewModel viewModel;
    private ObservableList<ProductEntry> productEntries;
    private User user;

    public void addPackage(MouseEvent mouseEvent) {
        viewModel.goToAddPackage();
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colProductId.setCellValueFactory(new PropertyValueFactory<>("productID"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageID"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        productEntries = FXCollections.observableArrayList();

        product_table.setRowFactory(tv -> {
            TableRow<ProductEntry> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    ProductEntry clickedRow = row.getItem();
                    showProduct(clickedRow);
                }
            });
            return row;
        });
    }

    private void showProduct(ProductEntry clickedRow) {
        try {
            Stage productWindow = new Stage();
            FXMLLoader productViewLoader = new FXMLLoader(getClass().getResource("../ProductDescriptionView/ProductView.fxml"));
            Parent productViewRoot = productViewLoader.load();
            ProductViewController controller = productViewLoader.getController();
            controller.setDataFromRow(clickedRow);
            controller.setViewModel(viewModel);
            controller.setWindow(productWindow);
            viewModel.setDraggable(productWindow, productViewRoot);
            Scene productViewScene = new Scene(productViewRoot);
            productWindow.setScene(productViewScene);
            productWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void loadUserData(User user) {
        product_table.setItems(FXCollections.observableArrayList());
        productEntries = FXCollections.observableArrayList();
        List<Package> packageList = viewModel.getPackagesOfUser();
        for (Package pack : packageList) {
            addPackageToTable(pack);
        }
        product_table.setItems(productEntries);
    }


    private void addPackageToTable(Package pack) {
        for (Product product : pack.getProducts()) {
            ProductEntry productEntry = new ProductEntry(product);
            productEntry.setAvailability("All week");
            productEntry.setAddress(pack.getAddress());
            productEntry.setStartDate(pack.getStartDateString());
            productEntry.setEndDate(pack.getEndDateString());
            productEntries.add(productEntry);
        }
    }

    public void addToTable(Package aPackage) {
        System.out.println("add to table");
        addPackageToTable(aPackage);
    }

    public void signOut(MouseEvent mouseEvent) {
        this.user = null;
        productEntries = FXCollections.observableArrayList();
        product_table.setItems(productEntries);
//        viewModel.goToSignIn();
        viewModel.loguotUser();
        viewModel.goToSearchView();
    }

    public void deleteProductFromTable(String owner_email, int packageID, int productID) {
        ObservableList<ProductEntry> entries = FXCollections.observableArrayList(this.productEntries);
        for (int i = 0; i < entries.size(); i++) {
            ProductEntry p = entries.get(i);
            if(p.getProductID() == productID && p.getOwnerEmail().equals(owner_email) && p.getPackageID() == packageID){
                productEntries.remove(i);
                break;
            }
        }
    }

    public void deletePackageFromTable(Package pack) {
        for (Product p : pack.getProducts()) {
            deleteProductFromTable(p.ownerEmail, p.packageID, p.productID);
        }
    }

    public void goToSearchView(MouseEvent mouseEvent) {
        viewModel.goToSearchView();
    }
}
