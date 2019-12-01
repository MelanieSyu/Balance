package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import repository.UserRepository;

/**
 * FXML Controller class
 *
 * @author vahid
 */
public class LoginController implements Initializable {

    UserRepository userRepository = new UserRepository();
    @FXML
    private TextField usernamefield;
    @FXML
    private PasswordField passwordfield;
    @FXML
    private Button loginBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button registerBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonVisible();
    }

    public void buttonVisible() {
        User user = userRepository.findUser();
        if (user == null) {
            registerBtn.setVisible(true);
            loginBtn.setVisible(false);
            deleteBtn.setVisible(false);
        } else {
            registerBtn.setVisible(false);
            loginBtn.setVisible(true);
            deleteBtn.setVisible(true);
        }
    }

    @FXML
    public void loginUser() {
        String username = usernamefield.getText().trim();
        String password = passwordfield.getText().trim();
        User user = userRepository.findUser();
        if (!username.isEmpty() && !password.isEmpty()) {
            if (password.equals(user.getPassword())) {
                if (username.equals(user.getUsername())) {

                    try {
                        Stage stage = new Stage();
                        Parent root = new FXMLLoader(getClass().getResource("/view/MainView.fxml")).load();
                        stage.setTitle("Main view");
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        closeThisWindow();
                        stage.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect username!!", ButtonType.OK);
                    alert.show();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect password!!", ButtonType.OK);
                alert.show();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fill all the fields!!", ButtonType.OK);
            alert.show();

        }

    }

    @FXML
    public void deleteUser() {
        String username = usernamefield.getText().trim();
        String password = passwordfield.getText().trim();
        User user = userRepository.findUser();
        if (!username.isEmpty() && !password.isEmpty()) {
            if (password.equals(user.getPassword())) {
                if (username.equals(user.getUsername())) {

                    Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                    dialog.setHeaderText("Do you really want remove this user??");
                    final Optional<ButtonType> result = dialog.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        userRepository.deleteUser();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "User succesfully deleted", ButtonType.OK);
                        alert.showAndWait();
                        buttonVisible();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect username!!", ButtonType.OK);
                    alert.show();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect password!!", ButtonType.OK);
                alert.show();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fill all the fields!!", ButtonType.OK);
            alert.show();

        }
    }

    @FXML
    public void registerUser() {
        closeThisWindow();
        try {
            Stage stage = new Stage();
            Parent root = new FXMLLoader(getClass().getResource("/view/RegisterView.fxml")).load();

            Scene scene = new Scene(root);
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void closeThisWindow() {
        Stage thisStage = (Stage) registerBtn.getScene().getWindow();
        thisStage.close();

    }
}
