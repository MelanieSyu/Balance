/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author admin
 */
public class Plan {
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private int totalAmount;
    private String categoryName;
    private int CategoryAllotmentAmount;
    private int CategorySpentAmount;
    private double progress;
    private double percent;
    private String temp;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryAllotmentAmount() {
        return CategoryAllotmentAmount;
    }

    public void setCategoryAllotmentAmount(int CategoryAllotmentAmount) {
        this.CategoryAllotmentAmount = CategoryAllotmentAmount;
    }

    public int getCategorySpentAmount() {
        return CategorySpentAmount;
    }

    public void setCategorySpentAmount(int CategorySpentAmount) {
        this.CategorySpentAmount = CategorySpentAmount;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getPercent() {
        return percent;
    }

    public void setProccent(double progress) {
        this.percent = (int)Math.round(progress*100);
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(double progress) {
        progress*=100;
        if(progress>=0 && progress<=33){
            this.temp ="Good";
        }
        if(progress>33 && progress<=66){
            this.temp ="Normal";
        }
        if(progress>66 && progress<=100){
            this.temp ="Very Bad";
        }
        
        this.temp = temp;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
}
