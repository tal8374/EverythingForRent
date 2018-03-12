package View.AddPackageView;

import App.Address;
import Main.ViewModel;
import View.AbstractController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sun.font.TextRecord;

import java.time.LocalDate;
import java.util.List;

public class AddPackageController extends AbstractController{
    //    public TextField address;
    public TextField city;
    public TextField neighborhood;
    public TextField street;
    @FXML
    private DatePicker end_date;
    @FXML
    private DatePicker start_date;

    @FXML
    ComboBox<String> package_cancelation_policiy;

    private ViewModel viewModel;

    public void addNewProduct(MouseEvent mouseEvent) {
        if (!IsInputLegal()) {
            return;
        }

        String cancellationPolicy = package_cancelation_policiy.getValue();
        Address address = new Address(city.getText(), neighborhood.getText(), street.getText());
        viewModel.createNewPackage(address, cancellationPolicy, start_date.getValue(), end_date.getValue());
        viewModel.goToAddProduct();
        System.out.println("Adding new Package");
    }

    private boolean IsInputLegal() {
        if (!isEndDayLegal() || !isStartDayLegal()) {
            if (!isStartDayLegal()) {
                this.printMessageToUser("Start day is not legal. it most be not before today");
                return false;
            } else if (!isEndDayLegal()) {
                this.printMessageToUser("End day is not legal. it most be after start day");
                return false;
            } else if (!isAddressExist()) {
                this.printMessageToUser("The address is not exist.");
                return false;
            }
        }

        return true;
    }

    public void quitOption(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToUserView(MouseEvent mouseEvent) {
        viewModel.discartPackage();
        viewModel.goToUserView();
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;

        initCancellationPolicy(viewModel);
    }

    private void initCancellationPolicy(ViewModel viewModel) {
        ObservableList<String> packageCancelationPolicy = FXCollections.observableArrayList();
        List<String> allPolicies = viewModel.getAllPackageCancellationPolicy();
        packageCancelationPolicy.addAll(allPolicies);
        package_cancelation_policiy.setItems(packageCancelationPolicy);
        package_cancelation_policiy.getSelectionModel().selectFirst();
    }

    public void addNewPackage(MouseEvent mouseEvent) {
        viewModel.savePackage();
        viewModel.goToUserView();
    }

    private boolean isEndDayLegal() {
        return this.end_date.getValue() != null && this.end_date.getValue().isAfter(this.start_date.getValue());
    }

    private boolean isStartDayLegal() {
        return this.end_date.getValue() != null && this.end_date.getValue() != null &&
                this.start_date.getValue().isAfter(LocalDate.now().minusDays(1));
    }

    private void printMessageToUser(String messageContent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(messageContent);
        alert.showAndWait();
    }

    //    private boolean isAddressExist() {
//        return !this.address.getText().isEmpty();
//    }
    private boolean isAddressExist() {
        return !this.city.getText().isEmpty()&&!this.neighborhood.getText().isEmpty()&&!this.street.getText().isEmpty();
    }
}
