package View.ProductDescriptionView;

import App.Address;
import App.Product;
import View.UserViewScreen.ProductEntry;
import Main.ViewModel;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ProductViewController
{
    public TextField category;
    public TextField city;
    public TextField neighborhood;
    public TextField street;
    public TextArea  description;
    public TextField price;
    public Label owner_email;
    public JFXButton delete_btn;
    public JFXButton update_btn;
    public JFXButton save_changes_btn;
    public TextField startDate;
    public TextField endDate;
    private ViewModel viewModel;
    private Stage window;
    private int productID;
    private int packageID;

    public void closeWindow(MouseEvent mouseEvent) {
        window.close();
    }

    public void setDataFromRow(ProductEntry clickedRow) {
        productID = clickedRow.getProductID();
        packageID = clickedRow.getPackageID();
        category.setText(clickedRow.getCategory());
        description.setText(clickedRow.getDescription());
        price.setText(Integer.toString(clickedRow.getPrice()));
        owner_email.setText(clickedRow.getOwnerEmail());
        city.setText(clickedRow.getAddress().getCity());
        neighborhood.setText(clickedRow.getAddress().getNeighborhood());
        street.setText(clickedRow.getAddress().getStreet());
        startDate.setText(clickedRow.getStartDate());
        endDate.setText(clickedRow.getEndDate());
    }

    public void deleteProduct(MouseEvent mouseEvent) {
        viewModel.deleteProduct(owner_email.getText(), packageID, productID);
        closeWindow(mouseEvent);
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    public void updateProduct(MouseEvent mouseEvent) {
        delete_btn.setDisable(true);
        save_changes_btn.setDisable(false);
        category.setEditable(true);
        city.setEditable(true);
        neighborhood.setEditable(true);
        street.setEditable(true);
//        description.setEditable(true);
        price.setEditable(true);
    }

    public void saveChanges(MouseEvent mouseEvent) {
        Product prod = new Product(owner_email.getText(), productID, packageID,Integer.parseInt(price.getText()),category.getText());
        prod.description = description.getText();
//        viewModel.updateProduct(prod, address.getText());
        viewModel.updateProduct(prod, new Address(city.getText(), neighborhood.getText(),street.getText()));
        delete_btn.setDisable(false);
        save_changes_btn.setDisable(true);
        category.setEditable(false);
        city.setEditable(false);
        neighborhood.setEditable(false);
        street.setEditable(false);
        description.setEditable(false);
        price.setEditable(false);
    }

    public void setForViewOnly() {
        delete_btn.setDisable(true);
        delete_btn.setStyle("-fx-text-fill: #2D3447");
        save_changes_btn.setDisable(true);
        save_changes_btn.setStyle("-fx-text-fill: #2D3447");
        update_btn.setDisable(true);
        update_btn.setStyle("-fx-text-fill: #2D3447");
        category.setEditable(false);
        city.setEditable(false);
        neighborhood.setEditable(false);
        street.setEditable(false);
        description.setEditable(false);
        price.setEditable(false);
    }
}
