/**
 * @author Shane Hagan
 * Date: 3/16/2025
 * Project: Personal Finance Tracker
 * Class: SavingsGoal model class - our POJO with variables, table creation, constructors, setters / getters,
 * hashCode & equals methods, and toString method.
 * Tracks savings goals and progress toward those goals tied to a specific user.
 */

package com.shanehagan.personalfinance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goalId", nullable = false, unique = true)
    private int goalId;

    @Column(name = "goalName", length = 100, nullable = false)
    private String goalName;

    @Column(name = "targetAmount", nullable = false)
    private double targetAmount;

    @Column(name = "currentAmount", nullable = false)
    private double currentAmount;

    @Column(name = "targetDate")
    private LocalDate targetDate;

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

    public SavingsGoal() {}

    public SavingsGoal(String goalName, double targetAmount, double currentAmount,
                       LocalDate targetDate, String notes, User user) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
        this.notes = notes;
        this.user = user;
    }

    public int getGoalId() { return goalId; }
    public void setGoalId(int goalId) { this.goalId = goalId; }

    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavingsGoal that = (SavingsGoal) o;
        return goalId == that.goalId && Double.compare(that.targetAmount, targetAmount) == 0
                && Double.compare(that.currentAmount, currentAmount) == 0
                && goalName.equals(that.goalName) && Objects.equals(targetDate, that.targetDate)
                && Objects.equals(notes, that.notes) && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalId, goalName, targetAmount, currentAmount, targetDate, notes, user);
    }

    @Override
    public String toString() {
        return "SavingsGoal{" +
                "goalId=" + goalId +
                ", goalName='" + goalName + '\'' +
                ", targetAmount=" + targetAmount +
                ", currentAmount=" + currentAmount +
                ", targetDate=" + targetDate +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                '}';
    }
}
