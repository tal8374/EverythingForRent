package View.SignUpScreenView;/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;

import App.User;
import Main.ViewModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author proxc
 */
public class SignUpController implements Initializable {

    @FXML
    private Label btn_exit;
    public JFXTextField first_name;
    public JFXPasswordField password;
    public JFXPasswordField confirm_password;
    public JFXTextField last_name;
    public JFXTextField email;
    public JFXButton btn_add_user;

    private ViewModel viewModel;

    @FXML
    private void exitApp(MouseEvent event) {
      System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void handleAddUser(MouseEvent mouseEvent) {
        User u = new User(first_name.getText(),last_name.getText(), password.getText(), email.getText());
        if(!formCorrect())
        {
            if(password.getText().equals(confirm_password.getText()))
                popAlert("One of th field is missing");
            else
                popAlert("These passwords don't match");
        }
        else if(!viewModel.isUserExists(u)) {
            try {
                viewModel.sendEmail(email.getText());
                viewModel.addUser(u);
                resetFields(mouseEvent);
                viewModel.goToSignIn();
            }
            catch (Exception e){
                popAlert("Email is not well formed");
            }
        }
        else {
            popAlert("Email is in use");
        }
    }

    private void popAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    private boolean formCorrect() {
        Boolean name = !first_name.getText().equals("") && !last_name.getText().equals("");
        Boolean data = !email.getText().equals("") && !password.getText().equals("");
        Boolean pass = confirm_password.getText().equals(password.getText());
        return name && data && pass;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void goToSignIn(MouseEvent mouseEvent) {
        viewModel.goToSignIn();
    }

    public void resetFields(MouseEvent mouseEvent) {
        first_name.setText("");
        password.setText("");
        last_name.setText("");
        email.setText("");
        confirm_password.setText("");
    }

}
