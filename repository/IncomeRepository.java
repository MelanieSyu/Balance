package repository;

import db.DbConnection;
import java.sql.Date;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Income;
import model.IncomeCategory;

/**
 *
 * @author vahid
 */
public class IncomeRepository extends DbConnection implements Repository<Income> {

    @Override
    public void add(Income income) {
        String query = "insert into income (name,date,amount,category_id) values (?,?,?,?)";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, income.getName());
            preparedStatement.setDate(2, (Date) income.getDate());
            preparedStatement.setInt(3, income.getAmount());
            preparedStatement.setInt(4, income.getCategory().getId());
            preparedStatement.execute();

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public List<Income> findAll() {
        List<Income> incomeList = new ArrayList<>();
        String query = "select * from income inner join income_category on income.category_id = income_category.id where income.status = 1 and income_category.status=1";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Income income = new Income();
                IncomeCategory category = new IncomeCategory();
                income.setName(resultSet.getString("income.name"));
                income.setAmount(resultSet.getInt("income.amount"));
                income.setDate(resultSet.getDate("income.date"));
                category.setId(resultSet.getInt("income_category.id"));
                category.setName(resultSet.getString("income_category.name"));
                income.setCategory(category);
                incomeList.add(income);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return incomeList;

    }

    public List<Income> findByDate(Date startDate, Date endDate) {

        List<Income> incomeList = new ArrayList<>();
        String query = "SELECT * FROM income inner join income_category on income.category_id=income_category.id where date between '" + startDate + "' and '" + endDate + "' and income.status=1 and income_category.status=1;";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Income income = new Income();
                IncomeCategory category = new IncomeCategory();
                income.setName(resultSet.getString("income.name"));
                income.setAmount(resultSet.getInt("income.amount"));
                income.setDate(resultSet.getDate("income.date"));
                category.setId(resultSet.getInt("income_category.id"));
                category.setName(resultSet.getString("income_category.name"));
                income.setCategory(category);
                incomeList.add(income);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return incomeList;

    }

}
