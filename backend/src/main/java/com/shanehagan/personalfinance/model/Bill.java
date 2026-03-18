/**
 * @author Shane Hagan
 * Date: 3/16/2025
 * Project: Personal Finance Tracker
 * Class: Bill model class - our POJO with variables, table creation, constructors, setters / getters,
 * hashCode & equals methods, and toString method.
 * Tracks recurring bills and their due dates tied to a specific user.
 */

package com.shanehagan.personalfinance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billId", nullable = false, unique = true)
    private int billId;

    @Column(name = "billName", length = 100, nullable = false)
    private String billName;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "dueDate", nullable = false)
    private LocalDate dueDate;

    @JsonProperty("recurring")
    @Column(name = "isRecurring")
    private boolean recurring;

    @JsonProperty("paid")
    @Column(name = "isPaid")
    private boolean paid;

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

    public Bill() {}

    public Bill(String billName, String category, double amount, LocalDate dueDate,
                boolean recurring, boolean paid, String notes, User user) {
        this.billName = billName;
        this.category = category;
        this.amount = amount;
        this.dueDate = dueDate;
        this.recurring = recurring;
        this.paid = paid;
        this.notes = notes;
        this.user = user;
    }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public String getBillName() { return billName; }
    public void setBillName(String billName) { this.billName = billName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    @JsonProperty("recurring")
    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }

    @JsonProperty("paid")
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return billId == bill.billId && Double.compare(bill.amount, amount) == 0
                && recurring == bill.recurring && paid == bill.paid
                && billName.equals(bill.billName) && Objects.equals(category, bill.category)
                && dueDate.equals(bill.dueDate) && Objects.equals(notes, bill.notes)
                && user.equals(bill.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, billName, category, amount, dueDate, recurring, paid, notes, user);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billName='" + billName + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", isRecurring=" + recurring +
                ", isPaid=" + paid +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                '}';
    }
}