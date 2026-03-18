/**
 * @author Shane Hagan
 * Date: 3/16/2025
 * Project: Personal Finance Tracker
 * Class: Transaction model class - our POJO with variables, table creation, constructors, setters / getters,
 * hashCode & equals methods, and toString method.
 * Tracks income and expense transactions tied to a specific user.
 */

package com.shanehagan.personalfinance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txnId", nullable = false, unique = true)
    private int txnId;

    @Column(name = "transactionName", length = 100, nullable = false)
    private String transactionName;

    @Column(name = "transactionType", length = 50, nullable = false)
    private String transactionType;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

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

    public Transaction() {}

    public Transaction(String transactionName, String transactionType, String category,
                       double amount, LocalDate date, String notes, User user) {
        this.transactionName = transactionName;
        this.transactionType = transactionType;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
        this.user = user;
    }

    public int getTxnId() { return txnId; }
    public void setTxnId(int txnId) { this.txnId = txnId; }

    public String getTransactionName() { return transactionName; }
    public void setTransactionName(String transactionName) { this.transactionName = transactionName; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction transaction = (Transaction) o;
        return txnId == transaction.txnId && Double.compare(transaction.amount, amount) == 0
                && transactionName.equals(transaction.transactionName)
                && transactionType.equals(transaction.transactionType)
                && Objects.equals(category, transaction.category)
                && date.equals(transaction.date)
                && Objects.equals(notes, transaction.notes)
                && user.equals(transaction.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txnId, transactionName, transactionType, category, amount, date, notes, user);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "txnId=" + txnId +
                ", transactionName='" + transactionName + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                '}';
    }
}
