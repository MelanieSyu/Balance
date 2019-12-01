/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import model.Expense;
import model.Income;
import repository.ExpenseRepository;
import repository.IncomeRepository;

/**
 *
 * @author admin
 */
public class Operation {

    public static int getBalance() { // статичные методы не нужд в созд объекта
        int totalBalance = 0;
        int incomeBalance = 0;
        int expenseBalance = 0;

        IncomeRepository incomeRepository = new IncomeRepository();
        ExpenseRepository expenseRepository = new ExpenseRepository();
        List<Income> incomeList = incomeRepository.findAll();
        List<Expense> expenseList = expenseRepository.findAll();

        for (Income i : incomeList) {
            incomeBalance = incomeBalance + i.getAmount();
        }
        for (Expense e : expenseList) {
            expenseBalance = expenseBalance + e.getAmount();
        }
        totalBalance = incomeBalance - expenseBalance;
        return totalBalance;
    }

}
