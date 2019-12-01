/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ExpenseCategory;
import model.Expense;
import repository.ExpenseRepository;

/**
 * FXML Controller class
 *
 * @author vahid
 */
public class ExpenseDetailController implements Initializable {

    ExpenseRepository repository;

    @FXML
    private Label totalAmountLabel;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableColumn<Expense, String> nameColumn;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TableColumn<Expense, Integer> amountColumn;
    @FXML
    private TableView<Expense> expenseView;
    @FXML
    private TableColumn<Expense, ExpenseCategory> CategoryColumn;
    @FXML
    private TableColumn<Expense, DatePicker> dateColumn;
    @FXML
    private Label countLabel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setValues();
        fillTable();

    }

    public void setValues() {//определяет, какое поле будут использоваться для конкретного столбца в таблице.
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        CategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

    }

    public void fillTable() {
        repository = new ExpenseRepository();
        List<Expense> expenseList = repository.findAll();
        expenseView.getItems().setAll(expenseList);
        countLabel.setText("Count : " + expenseList.size());
        int total = 0;
        for (Expense i : expenseList) {
            total = total + i.getAmount();
        }
        totalAmountLabel.setText("Total amount : " + total);
    }

    @FXML
    private void filterByDate() {
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            java.sql.Date startDate = java.sql.Date.valueOf(startDatePicker.getValue());
            java.sql.Date endDate = java.sql.Date.valueOf(endDatePicker.getValue());
            if (endDate.after(startDate)) {
                repository = new ExpenseRepository();
                List<Expense> expenseList = repository.findByDate(startDate, endDate);
                expenseView.getItems().setAll(expenseList);
                countLabel.setText("Count : " + expenseList.size());
                int total = 0;
                for (Expense i : expenseList) {
                    total = total + i.getAmount();
                }
                totalAmountLabel.setText("Total amount : " + total);

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please input valid date", ButtonType.OK);
                alert.show();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill both of date", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    private void refresh() {
        fillTable();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

}
