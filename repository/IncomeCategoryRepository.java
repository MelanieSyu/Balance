package repository;

import db.DbConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.IncomeCategory;

/**
 *
 * @author vahid
 */
public class IncomeCategoryRepository extends DbConnection implements Repository<IncomeCategory> {

    @Override
    public void add(IncomeCategory category) {
        String query = "insert into income_category (name) values (?)";
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

    public void update(IncomeCategory category) {
        String query = "update income_category set name=? where id=?";
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

    public void delete(IncomeCategory category) {
        String query = "update income_category set status=0 where id=?";
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
    public List<IncomeCategory> findAll() {
        List<IncomeCategory> categoryList = new ArrayList<>();
        String query = "SELECT * FROM income_category where status =1";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IncomeCategory category = new IncomeCategory();
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

    public List<IncomeCategory> findForChart() {
        List<IncomeCategory> categoryList = new ArrayList<>();
        String query = "select income_category.name, sum(amount) as sum  from income  inner join income_category on income.category_id=income_category.id where income.status=1 and income_category.status=1 group by (income_category.name);";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IncomeCategory category = new IncomeCategory();
                category.setName(resultSet.getString("income_category.name"));
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
}
