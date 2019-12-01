package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Income;
import model.IncomeCategory;
import repository.IncomeRepository;

/**
 * FXML Controller class
 *
 * @author vahid
 */
public class RegisterIncomeController implements Initializable {

    MainController mainController;
    IncomeCategory incomeCategory;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        incomeCategory = mainController.incomeCategory;
        categoryLabel.setText("Selected category: " + incomeCategory.getName());
    }
    @FXML
    private Label categoryLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker.setValue(LocalDate.now());
        makeNumeric();
    }

    public void makeNumeric() {
        amountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,5}(\\d{0,0})?")) {
                    amountField.setText(oldValue);
                }
            }
        });
    }

    @FXML
    private void registerIncomeCategory() {
        IncomeRepository repository = new IncomeRepository();
        String amount = amountField.getText().trim();

        if (!nameField.getText().trim().isEmpty() && !amount.isEmpty() && Integer.parseInt(amount) > 0
                && datePicker.getValue() != null) {
            Income income = new Income();
            income.setName(nameField.getText().trim());
            income.setAmount(Integer.parseInt(amount));
            java.sql.Date dateFormat = java.sql.Date.valueOf(datePicker.getValue());
            income.setDate(dateFormat);
            income.setCategory(incomeCategory);
            repository.add(income);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Income succesfully add", ButtonType.OK);
            alert.showAndWait();
            closeThisWindow();
            mainController.fillIncomePieChart();
            mainController.setTotalAmount();
            mainController.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill all the fields and set amount greater 0.", ButtonType.OK);
            alert.show();
        }

    }

    public void closeThisWindow() {
        Stage thisStage = (Stage) categoryLabel.getScene().getWindow(); // делаем каст
        thisStage.close();
    }

}
