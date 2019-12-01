package model;

import java.sql.Date;

/**
 *
 * @author vahid
 */
public class Expense {

    private int id;
    private String name;
    private Date date;
    private int Amount;
    private ExpenseCategory category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int Amount) {
        this.Amount = Amount;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Expense{" + "id=" + id + ", name=" + name + ", date=" + date + ", Amount=" + Amount + ", category=" + category + '}';
    }

}
