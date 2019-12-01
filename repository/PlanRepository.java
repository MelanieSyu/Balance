/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import db.DbConnection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ExpenseCategory;
import model.Plan;

public class PlanRepository extends DbConnection {

    public void add(Plan plan, List<ExpenseCategory> list) {
        String query = "insert into plan(name, start_date, end_date, category_id, category_amount, total_amount) values(?, ?, ?, ?, ?, ?)";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            for (ExpenseCategory category : list) {
                preparedStatement.setString(1, plan.getName());
                preparedStatement.setDate(2, (Date) plan.getStartDate());
                preparedStatement.setDate(3, (Date) plan.getEndDate());
                preparedStatement.setInt(4, category.getId());
                preparedStatement.setInt(5, category.getAmount());
                preparedStatement.setInt(6, plan.getTotalAmount());
                preparedStatement.execute();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Plan> findAll() {
        List<Plan> planList = new ArrayList<>();
        String query = "select * from plan where status = 1 group by(name)";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Plan plan = new Plan();
                plan.setName(resultSet.getString("name"));
                planList.add(plan);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return planList;
    }

    public List<Plan> findForSelected(String planName) {
        List<Plan> planList = new ArrayList<>();
        String query = "SELECT sum(expense.amount) as sum ,  plan.name , expense_category.name , plan.amount , plan.start_date , plan.end_date , plan.total_amount FROM plan left join expense_category on plan.category_id =  expense_category.id left join expense on plan.category_id = expense.category_id and  expense.date between plan.start_date and plan.end_date where plan.status = 1 and expense_category.status = 1 and plan.name = '" + planName + "' group by (expense_category.name) ";

        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Plan plan = new Plan();
                plan.setName(resultSet.getString("plan.name"));
                plan.setStartDate(resultSet.getDate("plan.start_date"));
                plan.setEndDate(resultSet.getDate("plan.end_date"));
                plan.setTotalAmount(resultSet.getInt("plan.total_amount"));
                plan.setCategoryName(resultSet.getString("expense_category.name"));
                plan.setCategoryAllotmentAmount(resultSet.getInt("plan.amount"));
                plan.setCategorySpentAmount(resultSet.getInt("sum"));
                double progress = (double)plan.getCategorySpentAmount()/plan.getCategoryAllotmentAmount();
                
                
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        return planList;
    }
}
