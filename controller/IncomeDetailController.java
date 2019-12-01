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
import model.Income;
import model.IncomeCategory;
import repository.IncomeRepository;

/**
 * FXML Controller class
 *
 * @author vahid
 */
public class IncomeDetailController implements Initializable {

    IncomeRepository repository;

    @FXML
    private Label totalAmountLabel;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableColumn<Income, String> nameColumn;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TableColumn<Income, Integer> amountColumn;
    @FXML
    private TableView<Income> incomeView;
    @FXML
    private TableColumn<Income, IncomeCategory> CategoryColumn;
    @FXML
    private TableColumn<Income, DatePicker> dateColumn;
    @FXML
    private Label countLabel;

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
        repository = new IncomeRepository();
        List<Income> incomeList = repository.findAll();
        incomeView.getItems().setAll(incomeList);
        countLabel.setText("Count : " + incomeList.size());
        int total = 0;
        for (Income i : incomeList) {
            total = total + i.getAmount();
        }
        totalAmountLabel.setText("Total amount : " + total);
    }

    @FXML
    public void filterByDate() {
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            java.sql.Date startDate = java.sql.Date.valueOf(startDatePicker.getValue());
            java.sql.Date endDate = java.sql.Date.valueOf(endDatePicker.getValue());
            if (endDate.after(startDate)) {
                repository = new IncomeRepository();
                List<Income> incomeList = repository.findByDate(startDate, endDate);
                incomeView.getItems().setAll(incomeList);
                countLabel.setText("Count : " + incomeList.size());
                int total = 0;
                for (Income i : incomeList) {
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
    public void refresh() {
        fillTable();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);

    }
}
