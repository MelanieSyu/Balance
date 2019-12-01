package repository;

import db.DbConnection;
import java.sql.Date;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Expense;
import model.ExpenseCategory;

/**
 *
 * @author vahid
 */
public class ExpenseRepository extends DbConnection implements Repository<Expense> {

    @Override
    public void add(Expense expense) {
        String query = "insert into expense (name,date,amount,category_id) values (?,?,?,?)";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, expense.getName());
            preparedStatement.setDate(2, (Date) expense.getDate());
            preparedStatement.setInt(3, expense.getAmount());
            preparedStatement.setInt(4, expense.getCategory().getId());
            preparedStatement.execute();

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public List<Expense> findAll() {
        List<Expense> expenseList = new ArrayList<>();
        String query = "select * from expense inner join expense_category on expense.category_id = expense_category.id where expense.status = 1 and expense_category.status=1";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Expense expense = new Expense();
                ExpenseCategory category = new ExpenseCategory();
                expense.setName(resultSet.getString("expense.name"));
                expense.setAmount(resultSet.getInt("expense.amount"));
                expense.setDate(resultSet.getDate("expense.date"));
                category.setId(resultSet.getInt("expense_category.id"));
                category.setName(resultSet.getString("expense_category.name"));
                expense.setCategory(category);
                expenseList.add(expense);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return expenseList;

    }

    public List<Expense> findByDate(Date startDate, Date endDate) {

        List<Expense> expenseList = new ArrayList<>();
        String query = "SELECT * FROM expense inner join expense_category on expense.category_id=expense_category.id where date between '" + startDate + "' and '" + endDate + "' and expense.status=1 and expense_category.status=1;";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Expense expense = new Expense();
                ExpenseCategory category = new ExpenseCategory();
                expense.setName(resultSet.getString("expense.name"));
                expense.setAmount(resultSet.getInt("expense.amount"));
                expense.setDate(resultSet.getDate("expense.date"));
                category.setId(resultSet.getInt("expense_category.id"));
                category.setName(resultSet.getString("expense_category.name"));
                expense.setCategory(category);
                expenseList.add(expense);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return expenseList;

    }

}
