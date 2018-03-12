package View.PackageDescriptionView;

import App.Package;
import App.Product;
import Main.ViewModel;
import View.ProductDescriptionView.ProductViewController;
import View.UserViewScreen.ProductEntry;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PackageDescriptionView implements Initializable{
    public TreeTableView<ProductEntry> packageTable;
    public TreeTableColumn<ProductEntry, String> colPackageId;
    public TreeTableColumn<ProductEntry, String> colProductId;
    public TreeTableColumn<ProductEntry, String> colCategory;
    public TreeTableColumn<ProductEntry, String> colDescription;
    public TreeTableColumn<ProductEntry, String> colPrice;
    public TreeTableColumn<ProductEntry, String> colStartDate;
    public TreeTableColumn<ProductEntry, String> colEndDate;
    public TreeTableColumn<ProductEntry, String> colOwnerEmail;
    public Button rentBtn;
    public Button tradeBtn;
    public Button loginBtn;
    public Button userViewBtn;
    private ViewModel viewModel;
    private TreeItem<ProductEntry> root;
    private ProductEntry clickedProductRow;
    private ProductEntry renterPackage;
    private ObservableList<TreeItem<ProductEntry>> items;
    private Boolean chooseRow = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUserLoggedOut();
        colPackageId.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(Integer.toString(param.getValue().getValue().getPackageID()))
        );

        colProductId.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(Integer.toString(param.getValue().getValue().getProductID()))
        );

        colPrice.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(Integer.toString(param.getValue().getValue().getPrice()))
        );

        colCategory.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getCategory())
        );

        colDescription.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getDescription())
        );

        colStartDate.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getStartDate())
        );

        colEndDate.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getEndDate())
        );

        colOwnerEmail.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ProductEntry, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getOwnerEmail())
        );

        packageTable.setRowFactory(tv -> {
            TreeTableRow<ProductEntry> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    System.out.println("double clicked");
                    ProductEntry clickedRow = row.getItem();
                    if(!clickedRow.getCategory().equals(""))
                        showProduct(clickedRow);
                }
                else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    System.out.println("one clicked");
                    clickedProductRow = row.getItem();
                }
                chooseRow = true;
            });
            return row;
        });

        root = new TreeItem<>(new ProductEntry());
//        root.setExpanded(true);
        packageTable.setRoot(root);
        packageTable.setShowRoot(false);

    }

    private void showProduct(ProductEntry clickedRow) {
        try {
            Stage productWindow = new Stage();
            FXMLLoader productViewLoader = new FXMLLoader(getClass().getResource("../ProductDescriptionView/ProductView.fxml"));
            Parent productViewRoot = productViewLoader.load();
            ProductViewController controller = productViewLoader.getController();
            controller.setForViewOnly();
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

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;

    }

    public void addPackagesToTable(List<Package> packages){
        root = new TreeItem<>(new ProductEntry());
//        root.setExpanded(true);
        packageTable.setShowRoot(false);
        packageTable.setRoot(root);
        for (Package p: packages) {
            root.getChildren().add(addPackageToTable(p));
        }
    }

    public void showUserPackages()
    {
        addPackagesToTable(viewModel.getUnOrderdPackagesOfUser());
    }

    private TreeItem<ProductEntry> addPackageToTable(Package pack) {
        ProductEntry packageRoot = new ProductEntry();
        packageRoot.setOwnerEmail(pack.getOwner_email());
        packageRoot.setPackageID(pack.getPackage_id());
        packageRoot.setPrice(pack.getTotal_price());
        packageRoot.setStartDate(pack.getStartDateString());
        packageRoot.setEndDate(pack.getEndDateString());
        TreeItem<ProductEntry> root = new TreeItem<>(packageRoot);
        for (Product product : pack.getProducts()) {
            ProductEntry productEntry = new ProductEntry(product);
            productEntry.setAvailability("All week");
            productEntry.setAddress(pack.getAddress());
            productEntry.setStartDate(pack.getStartDateString());
            productEntry.setEndDate(pack.getEndDateString());
            root.getChildren().add(new TreeItem<>(productEntry));
        }
        return root;
    }

    public void rentPackage(MouseEvent mouseEvent) {
        try {
            if (!chooseRow) {
                viewModel.popAlert("You need to pick package");
            } else if (viewModel.getUser().email.equals(clickedProductRow.getOwnerEmail()))
                viewModel.popAlert("You cant rent your package");
            else {
                viewModel.addRentOrder(clickedProductRow);
                deleteOrderedPackageFromTable(clickedProductRow);
            }
        }
        catch (Exception e){
            viewModel.popAlert("You need to pick package not a product");
        }
    }

    public void tradePackage(MouseEvent mouseEvent) {
        try {
            List<Package> unOrderdPackagesOfUser = viewModel.getUnOrderdPackagesOfUser();
            if (!chooseRow) {
                viewModel.popAlert("You need to pick package");
            } else if (unOrderdPackagesOfUser.size() > 0 && !viewModel.getUser().email.equals(clickedProductRow.getOwnerEmail())) {
                chooseRow = false;
                items = root.getChildren();
                renterPackage = clickedProductRow;
//                deleteOrderedPackageFromTable(clickedProductRow);
                addPackagesToTable(unOrderdPackagesOfUser);
                tradeBtn.setOnMousePressed(this::approveTrade);
                tradeBtn.setText("Approve");
                rentBtn.setDisable(true);
                userViewBtn.setDisable(true);
            } else {
                if (viewModel.getUser().email.equals(clickedProductRow.getOwnerEmail()))
                    viewModel.popAlert("You cant trade with yourself");
                else
                    viewModel.popAlert("You have no packages");
            }
        }
        catch (Exception e){
            viewModel.popAlert("You need to pick package not a product");
        }
    }

    protected void approveTrade(MouseEvent mouseEvent){
        try {
//            root.getChildren().removeAll();
//            root.getChildren().addAll(items);
            rentBtn.setDisable(false);
            userViewBtn.setDisable(false);
            tradeBtn.setOnMousePressed(this::tradePackage);
            tradeBtn.setText("Trade");
            viewModel.addTradeOrder(clickedProductRow);
            viewModel.addTradeOrder(renterPackage);
            deleteOrderedPackageFromTable(clickedProductRow);
            deleteOrderedPackageFromTable(renterPackage);
            items = null;
            renterPackage = null;
            viewModel.goToSearchView();
        }
        catch (Exception e)
        {
            viewModel.popAlert("You need to pick package not a product");
        }
    }

    private void deleteOrderedPackageFromTable(ProductEntry clickedProductRow) {
        int i = -1;
        for (TreeItem<ProductEntry> p: root.getChildren()) {
            if(p.getValue().getOwnerEmail().equals(clickedProductRow.getOwnerEmail()) && p.getValue().getPackageID() == this.clickedProductRow.getPackageID())
                break;
            i++;
        }
        if(i != -1)
            root.getChildren().remove(i);
    }

    public void logIn(MouseEvent mouseEvent) {
        viewModel.goToSignIn();
    }
    public void setUserLoggedIn(){
        rentBtn.setDisable(false);
        tradeBtn.setDisable(false);
        userViewBtn.setDisable(false);
        loginBtn.setDisable(true);
    }

    public void setUserLoggedOut(){
        rentBtn.setDisable(true);
        tradeBtn.setDisable(true);
        userViewBtn.setDisable(true);
        loginBtn.setDisable(false);
    }

    public void goToUserView(MouseEvent mouseEvent) {
        viewModel.goToUserView();
    }

    public void Backtosearch(MouseEvent mouseEvent) {
        viewModel.goToSearchView();
    }
}
