package repository;

import db.DbConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ExpenseCategory;

/**
 *
 * @author vahid
 */
public class ExpenseCategoryRepository extends DbConnection implements Repository<ExpenseCategory> {

    @Override
    public void add(ExpenseCategory category) {
        String query = "insert into expense_category (name) values (?)";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category.getName());
            preparedStatement.execute();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }

    }

    public void update(ExpenseCategory category) {
        String query = "update expense_category set name=? where id=?";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());
            preparedStatement.execute();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public void delete(ExpenseCategory category) {
        String query = "update expense_category set status=0 where id=?";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, category.getId());
            preparedStatement.execute();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public List<ExpenseCategory> findAll() {

        List<ExpenseCategory> categoryList = new ArrayList<>();
        String query = "SELECT * FROM expense_category where status=1";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ExpenseCategory category = new ExpenseCategory();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                categoryList.add(category);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return categoryList;
    }

    public List<ExpenseCategory> findForChart() {

        List<ExpenseCategory> categoryList = new ArrayList<>();
        String query = "select expense_category.name, sum(amount) as sum  from expense  inner join expense_category on expense.category_id=expense_category.id where expense.status=1 and expense_category.status=1 group by (expense_category.name);";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ExpenseCategory category = new ExpenseCategory();
                category.setName(resultSet.getString("expense_category.name"));
                category.setAmount(resultSet.getInt("sum"));
                categoryList.add(category);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();

        }
        return categoryList;
    }

    int getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    int getAmount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
