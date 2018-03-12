/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.SignInScreenView;

import java.net.URL;
import java.util.ResourceBundle;

import App.User;
import Main.ViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.mail.MessagingException;

/**
 *
 * @author danml
 */
public class SignInController implements Initializable {
    private int trailsForConnections = 0;
    public Label btn_exit;
    public TextField email;
    public PasswordField password;
    @FXML
    private Label label;
    private ViewModel viewModel;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void exitApp(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void goToSignUp(MouseEvent mouseEvent) {
        viewModel.goToSignUp();
    }

    public void goToUserView(MouseEvent mouseEvent) {
        if(viewModel.loadUser(email.getText(), password.getText())) {
//            viewModel.goToUserView();
            trailsForConnections = 0;
            viewModel.goToSearchView();
        }
        else {
            trailsForConnections++;
            if(trailsForConnections >= 3) {
                viewModel.popAlert("Email for retriving your password has been sent");
                trailsForConnections = 0;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Email or password are incorrect");
                alert.setContentText("You can sign up it free!");
                alert.showAndWait();
            }
        }
    }
}
