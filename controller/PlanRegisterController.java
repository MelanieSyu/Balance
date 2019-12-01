/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.ExpenseCategory;
import model.Plan;
import repository.ExpenseCategoryRepository;
import repository.PlanRepository;
import util.Operation;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class PlanRegisterController implements Initializable {

    ExpenseCategoryRepository expenseCategoryRepository;
    PlanRepository planRepository;

    int totalAmount;
    int currentBalance;

    @FXML
    private TableColumn<ExpenseCategory, String> nameCol;

    @FXML
    private TableColumn<ExpenseCategory, Integer> amountCol;

    @FXML
    private TableView<ExpenseCategory> categoryTableView;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label currentBalanceLabel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField planNameField;

    @FXML
    private TextField amountField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        totalAmountLabel.setText("Total amount :" + totalAmount);
        fillCategory();
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        currentBalanceLabel.setText("Current balance " + Operation.getBalance());
    }

    public void makeNumeric() {
        amountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,5}(\\d{0,0})?")) {
                    currentBalanceLabel.setText(oldValue);
                }
            }
        });
    }

    public void fillCategory() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseCategoryRepository = new ExpenseCategoryRepository();
        List<ExpenseCategory> categoryList = expenseCategoryRepository.findAll();
        categoryTableView.getItems().setAll(categoryList);
    }

    @FXML
    public void setAmount() {
        totalAmount = 0;
        ExpenseCategory selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            if (amountField.getText().trim().isEmpty()) {
                selectedCategory.setAmount(0); //чтобы стирал последнию цифру тоже 123  1
            } else {
                selectedCategory.setAmount(Integer.parseInt(amountField.getText()));
            }
            categoryTableView.getItems().set(categoryTableView.getSelectionModel().getSelectedIndex(), selectedCategory);
            //взять элементы кат и дать значение выбрн индексу
            for (ExpenseCategory e : categoryTableView.getItems()) {
                totalAmount += e.getAmount();
            }
            totalAmountLabel.setText("Total amount :" + totalAmount);
        }

    }

    @FXML
    public void onCategorySelect() {
        ExpenseCategory selectCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectCategory != null) {
            amountField.setText("");
        } else {
            amountField.setText(selectCategory.getAmount() + "");
        }
    }

    @FXML
    public void add() {
        if (!planNameField.getText().trim().isEmpty() && startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            java.sql.Date startDate = java.sql.Date.valueOf(startDatePicker.getValue());
            java.sql.Date endDate = java.sql.Date.valueOf(endDatePicker.getValue());
            if (endDate.after(startDate)) {
                boolean result = false;
                for (ExpenseCategory e : categoryTableView.getItems()) {
                    if (e.getAmount() >= 1) {
                        result = true;
                    } else {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    Plan plan = new Plan();
                    planRepository = new PlanRepository();
                    plan.setName(planNameField.getText().trim());
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setTotalAmount(totalAmount);
                    List<ExpenseCategory> expenseList = new ArrayList<>();
                    expenseList.addAll(categoryTableView.getItems());
                    if (totalAmount <= currentBalance) {
                        planRepository.add(plan, expenseList);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Plan sucessfully added", ButtonType.OK);
                        alert.showAndWait();

                        closeThisWindow();

                    } else {
                        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                        dialog.setHeaderText("The amount of your balance is lower than the allocated expenses. Continue anyway?");
                        final Optional<ButtonType> dialogResult = dialog.showAndWait();
                        if (dialogResult.get() == ButtonType.OK) {
                            planRepository.add(plan, expenseList);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Plan sucessfully added", ButtonType.OK);
                            alert.showAndWait();

                            closeThisWindow();

                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "Plan sucessfully added", ButtonType.OK);
                            alert.showAndWait();
                        }

                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid date", ButtonType.OK);
                alert.show();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill all the fields", ButtonType.OK);
            alert.show();
        }
    }

    public void closeThisWindow() {
        Stage thisStage = (Stage) totalAmountLabel.getScene().getWindow();
        thisStage.close();
    }
}
