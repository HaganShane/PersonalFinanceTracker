/**
 * @author Shane Hagan
 * Date: 3/16/2025
 * Project: Personal Finance Tracker
 * Class: Budget model class - our POJO with variables, table creation, constructors, setters / getters,
 * hashCode & equals methods, and toString method.
 * Tracks monthly budget allocations by category tied to a specific user.
 */

package com.shanehagan.personalfinance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budgetId", nullable = false, unique = true)
    private int budgetId;

    @Column(name = "category", length = 50, nullable = false)
    private String category;

    @Column(name = "budgetedAmount", nullable = false)
    private double budgetedAmount;

    @Column(name = "month", length = 20, nullable = false)
    private String month;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "notes", length = 255)
    private String notes;

    /**
     * Changed from ManyToOne to OneToOne, pointing back to the user class
     * Assisted in putting the userId in the model table to track which user inputs a given record.
     * Useful when grabbing all records by a userId
     */
    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    private User user;

    public Budget() {}

    public Budget(String category, double budgetedAmount, String month, int year, String notes, User user) {
        this.category = category;
        this.budgetedAmount = budgetedAmount;
        this.month = month;
        this.year = year;
        this.notes = notes;
        this.user = user;
    }

    public int getBudgetId() { return budgetId; }
    public void setBudgetId(int budgetId) { this.budgetId = budgetId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getBudgetedAmount() { return budgetedAmount; }
    public void setBudgetedAmount(double budgetedAmount) { this.budgetedAmount = budgetedAmount; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return budgetId == budget.budgetId && Double.compare(budget.budgetedAmount, budgetedAmount) == 0
                && year == budget.year && category.equals(budget.category)
                && month.equals(budget.month) && Objects.equals(notes, budget.notes)
                && user.equals(budget.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgetId, category, budgetedAmount, month, year, notes, user);
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budgetId=" + budgetId +
                ", category='" + category + '\'' +
                ", budgetedAmount=" + budgetedAmount +
                ", month='" + month + '\'' +
                ", year=" + year +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                '}';
    }
}
