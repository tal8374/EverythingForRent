package View.AddProductView;

import App.User;
import Main.ViewModel;
import View.AbstractController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddProductController extends AbstractController implements Initializable
{

    public TextField Price;
//    public TextField Category;
    public TextField description;
    public ComboBox<String> categories;
    private ViewModel viewModel;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        categories.set
    }

    public void setViewModel(ViewModel viewModel) {

        this.viewModel = viewModel;

        initCategories(viewModel);
    }

    private void initCategories(ViewModel viewModel) {
        ObservableList<String> categoriesOptions = FXCollections.observableArrayList();
        List<String> allCategories =  viewModel.getAllCategories();
        categoriesOptions.addAll(allCategories);
        categories.setItems(categoriesOptions);
        categories.getSelectionModel().selectFirst();
    }

    public void addProduct(MouseEvent mouseEvent) {
        try {
            viewModel.addProductToPackage(Integer.parseInt(Price.getText()), categories.getValue(), description.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("New product add to package");
            alert.showAndWait();
            Price.setText("");
            categories.getSelectionModel().selectFirst();
            description.setText("");
        }
        catch (Exception e){
            viewModel.popAlert("Price must be integer");
        }
    }


    public void goToPackageView(MouseEvent mouseEvent) {
        viewModel.goToAddPackage();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void quitOption(ActionEvent actionEvent) {
        System.exit(0);
    }
}
