package controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ExpenseCategory;
import model.IncomeCategory;
import model.Plan;
import repository.ExpenseCategoryRepository;
import repository.IncomeCategoryRepository;
import repository.PlanRepository;
import util.MathOperations;
import util.Operation;

/**
 *
 * @author vahid
 */
public class MainController implements Initializable {

    IncomeCategoryRepository incomeCategoryRepository = new IncomeCategoryRepository();

    //                    ==================
    //                       IncomeStarts
    //                    ==================
    @FXML
    private ListView<IncomeCategory> incomeListView;
    IncomeCategory incomeCategory;
    @FXML
    private TextField incomeNameField;
   
    @FXML
    private PieChart chart;

    @FXML
    Label totalBalanceLabel;

    //                 ===================
    //                    ExpenseStarts
    //                 ===================
    ExpenseCategoryRepository expenseCategoryRepository = new ExpenseCategoryRepository();
    @FXML
    private ListView<ExpenseCategory> expenseListView;
    ExpenseCategory expenseCategory;

    @FXML
    private TextField expenseNameField;
    

    //////////////////////////////////////////////////////////////PLAN
    PlanRepository planRepository = new PlanRepository();
     ProgressBar progressBar = new ProgressBar(50);
    @FXML
    Label planNameLabel;
    @FXML
    Label planAmountLabel;
    @FXML
    Label planStartDateLabel;
    @FXML
    Label planEndDateLabel;
    @FXML
    TableView<Plan> planTableView;
    @FXML
    TableColumn<Plan, String> categoryCol;
    @FXML
    TableColumn<Plan, Integer> allotmentAmountCol;
    @FXML
    TableColumn<Plan, Double> progressBarCol;
    @FXML
    TableColumn<Plan, Integer> categorySpentAmountCol;
    @FXML
    TableColumn<Plan, Double> procentCol;
    @FXML
    TableColumn<Plan, String> tempCol;
    @FXML
    HBox labelBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTotalAmount();
        fillIncomePieChart();
        fillExpensePieChart();
        refresh();

    }

    public void setTotalAmount() {
//        MathOperations mathOperation = new MathOperations(); //они вызов через класс
//        int totalAmount = mathOperation.getTotalAmount();
//        totalBalanceLabel.setText("Total balance: " + totalAmount);
        totalBalanceLabel.setText("Total balance: " + Operation.getBalance());
    }

    @FXML
    void refresh() {
        //////////////////////////////////////////////
        /////////////////////////////////////////////
        fillIncomeCategoryList();
        fillExpenseCategoryList();
        expenseNameField.setText("");
        incomeNameField.setText("");
    }

    public void fillIncomeCategoryList() {
        incomeCategoryRepository = new IncomeCategoryRepository();
        List<IncomeCategory> categoryList = incomeCategoryRepository.findAll();
        incomeListView.getItems().setAll(categoryList);
    }

    @FXML
    public void onIncomeCategorySelect() {
        IncomeCategory selectedCategory = incomeListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            incomeNameField.setText(selectedCategory.getName());
        }
    }

    @FXML
    public void saveIncomeCategory() {
        if (!incomeNameField.getText().trim().isEmpty()) {
            List<IncomeCategory> categoryList = incomeCategoryRepository.findAll();
            boolean unique = false;
            if (categoryList.isEmpty()) {
                unique = true;
            } else {
                for (IncomeCategory c : categoryList) {
                    if (incomeNameField.getText().trim().equalsIgnoreCase(c.getName())) {
                        unique = false;
                        break;
                    } else {
                        unique = true;
                    }
                }
            }
            if (unique) {
                IncomeCategory selectedCategory = incomeListView.getSelectionModel().getSelectedItem();
                if (selectedCategory == null) {
                    IncomeCategory category = new IncomeCategory();
                    category.setName(incomeNameField.getText().trim());
                    incomeCategoryRepository.add(category);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Category successfully added", ButtonType.OK);
                    alert.show();
                    refresh();
                } else {
                    IncomeCategory category = new IncomeCategory();
                    category.setId(selectedCategory.getId());
                    category.setName(incomeNameField.getText().trim());
                    incomeCategoryRepository.update(category);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Category successfully update", ButtonType.OK);
                    alert.show();
                    refresh();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This category exist", ButtonType.OK);
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill category name", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void deleteIncomeCategory() {
        IncomeCategory selectedCategory = incomeListView.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setHeaderText("Do you really want to remove category?");
            final Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                incomeCategoryRepository.delete(selectedCategory);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Category is successfully deleted", ButtonType.OK);
                alert.show();
                refresh();
            }
        }
    }

    @FXML
    private void gotoIncomeRegister() {
        IncomeCategory selectedCategory = incomeListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegisterIncomeView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                RegisterIncomeController controller = loader.getController();
                incomeCategory = selectedCategory;
                controller.setMainController(this);
                stage.setTitle("Register income");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select category", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    private void gotoIncomeDetail() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IncomeDetailView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Income detail");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void fillIncomePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<IncomeCategory> categoryList = incomeCategoryRepository.findForChart();
        for (IncomeCategory i : categoryList) {
            pieChartData.add(new PieChart.Data(i.getName(), i.getAmount()));
        }
        chart.setData(pieChartData);
    }
    
    
    
/////////////////////////////////////////////Expense
    public void fillExpenseCategoryList() {
        expenseCategoryRepository = new ExpenseCategoryRepository();
        List<ExpenseCategory> categoryList = expenseCategoryRepository.findAll();
        expenseListView.getItems().setAll(categoryList);
    }

    @FXML
    public void onExpenseCategorySelect() {
        ExpenseCategory selectedCategory = expenseListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            expenseNameField.setText(selectedCategory.getName());
        }
    }

    @FXML
    public void saveExpenseCategory() {
        if (!expenseNameField.getText().trim().isEmpty()) {
            List<ExpenseCategory> categoryList = expenseCategoryRepository.findAll();
            boolean unique = false;
            if (categoryList.isEmpty()) {
                unique = true;
            } else {
                for (ExpenseCategory c : categoryList) {
                    if (expenseNameField.getText().trim().equalsIgnoreCase(c.getName())) {
                        unique = false;
                        break;
                    } else {
                        unique = true;
                    }
                }
            }
            if (unique) {
                ExpenseCategory selectedCategory = expenseListView.getSelectionModel().getSelectedItem();
                if (selectedCategory == null) {
                    ExpenseCategory category = new ExpenseCategory();
                    category.setName(expenseNameField.getText().trim());
                    expenseCategoryRepository.add(category);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Category successfully added", ButtonType.OK);
                    alert.show();
                    refresh();
                } else {
                    ExpenseCategory category = new ExpenseCategory();
                    category.setId(selectedCategory.getId());
                    category.setName(expenseNameField.getText().trim());
                    expenseCategoryRepository.update(category);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Category successfully update", ButtonType.OK);
                    alert.show();
                    refresh();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This category exist", ButtonType.OK);
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill category name", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void deleteExpenseCategory() {
        ExpenseCategory selectedCategory = expenseListView.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setHeaderText("Do you really want to remove category?");
            final Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                expenseCategoryRepository.delete(selectedCategory);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Category is successfully deleted", ButtonType.OK);
                alert.show();
                refresh();
            }
        }
    }

    @FXML
    private void gotoExpenseRegister() {
        ExpenseCategory selectedCategory = expenseListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegisterExpenseView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                RegisterExpenseController controller = loader.getController();
                expenseCategory = selectedCategory;
                controller.setMainController(this);
                stage.setTitle("Register expense");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select category", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    private void gotoExpenseDetail() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExpenseDetailView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Expense detail");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void fillExpensePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<ExpenseCategory> categoryList = expenseCategoryRepository.findForChart();
        for (ExpenseCategory i : categoryList) {
            pieChartData.add(new PieChart.Data(i.getName(), i.getAmount()));
        }
        chart.setData(pieChartData);
    }


    ////////////////////////////Plan//////////////////////////////////////
    
        @FXML
        public void gotoPlan(){
    try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PlanRegister.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Plan");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
        
        public  void fillPlan(){
//            List<Plan> planList = planRepository.findAll();
//            plan
/////////////////////////////////////////////////////////////////////////////////////////
        }
}
