package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
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
import model.Expense;
import model.ExpenseCategory;
import repository.ExpenseRepository;
import util.Operation;

/**
 * FXML Controller class
 *
 * @author vahid
 */
public class RegisterExpenseController implements Initializable {

    MainController mainController;
    ExpenseCategory expenseCategory;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        expenseCategory = mainController.expenseCategory;
        categoryLabel.setText("Selected category: " + expenseCategory.getName());
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
    private void registerExpenseCategory() {
        ExpenseRepository repository = new ExpenseRepository();
        String amount = amountField.getText().trim();

        if (!nameField.getText().trim().isEmpty() && !amount.isEmpty() && Integer.parseInt(amount) > 0
                && datePicker.getValue() != null) {
            Expense expense = new Expense();
            expense.setName(nameField.getText().trim());
            expense.setAmount(Integer.parseInt(amount));
            java.sql.Date dateFormat = java.sql.Date.valueOf(datePicker.getValue());
            expense.setDate(dateFormat);
            expense.setCategory(expenseCategory);
            int totalBalance = Operation.getBalance();

            if (expense.getAmount() > totalBalance) {
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setHeaderText("You don't have enough balance. Continue anyway?");
                final Optional<ButtonType> result = dialog.showAndWait();
                if (result.get() == ButtonType.OK) {
                    repository.add(expense);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " It was  added, but you spent more money that you expected to spend)) ", ButtonType.OK);
                    alert.showAndWait();
                    closeThisWindow();
                    mainController.fillExpensePieChart();
                    mainController.setTotalAmount();
                    mainController.refresh();
                }
            } else {
                repository.add(expense);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Expense succesfully add", ButtonType.OK);
                alert.showAndWait();
                closeThisWindow();
                mainController.fillExpensePieChart();
                mainController.setTotalAmount();
                mainController.refresh();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Expense succesfully add", ButtonType.OK);
            alert.showAndWait();
            closeThisWindow();
            mainController.fillExpensePieChart();
            mainController.setTotalAmount();
            mainController.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill all the fields and set amount greater 0.", ButtonType.OK);
            alert.show();
        }

    }

    public void closeThisWindow() {
        Stage thisStage = (Stage) categoryLabel.getScene().getWindow();
        thisStage.close();
    }

}
