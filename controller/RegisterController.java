package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import repository.UserRepository;

/**
 * FXML Controller class
 *
 * @author vahid
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField namefield;
    @FXML
    private TextField surnamefield;
    @FXML
    private TextField usernamefield;
    @FXML
    private TextField passwordfield;
    @FXML
    private PasswordField password2field;
    @FXML
    private Button registerBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void registerUser() {
        UserRepository userRepository = new UserRepository();
        String name = namefield.getText().trim();
        String surname = surnamefield.getText().trim();
        String username = usernamefield.getText().trim();
        String password = passwordfield.getText().trim();
        String repeatpassword = password2field.getText().trim();
        if (!name.isEmpty() && !surname.isEmpty() && !username.isEmpty() && !password.isEmpty() && !repeatpassword.isEmpty()) {
            if (password.equalsIgnoreCase(repeatpassword)) {
                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setUsername(username);
                user.setPassword(password);
                userRepository.registerUser(user);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User succesfully registered");
                alert.showAndWait();
                closeThisWindow();
                try {
                    Stage stage = new Stage();
                    Parent root = new FXMLLoader(getClass().getResource("/view/LoginView.fxml")).load();

                    Scene scene = new Scene(root);
                    stage.setTitle("Login");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Password and repeat password doesn't match");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill all the fields!!");
            alert.show();
        }

    }

    public void closeThisWindow() {
        Stage thisStage = (Stage) registerBtn.getScene().getWindow();
        thisStage.close();
    }
}
