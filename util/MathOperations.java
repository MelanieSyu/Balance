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
 * @author vahid
 */
public class MathOperations {

    public int getTotalAmount() {
        int incomesTotal = 0;
        int expenseTotal = 0;
        int totalAmount = 0;

        IncomeRepository incomeRepository;
        incomeRepository = new IncomeRepository();
        ExpenseRepository expenseRepo = new ExpenseRepository();
        List<Income> incomeList = incomeRepository.findAll();
        List<Expense> expenseList = expenseRepo.findAll();
        for (Income i : incomeList) {
            incomesTotal += i.getAmount();
        }
        for (Expense e : expenseList) {
            expenseTotal += e.getAmount();
        }
        totalAmount = incomesTotal - expenseTotal;

        return totalAmount;

    }
}
