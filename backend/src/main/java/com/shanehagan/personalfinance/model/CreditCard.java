/**
 * @author Shane Hagan
 * Date: 3/16/2025
 * Project: Personal Finance Tracker
 * Class: CreditCard model class - our POJO with variables, table creation, constructors, setters / getters,
 * hashCode & equals methods, and toString method.
 * Tracks credit card purchases and spending tied to a specific user.
 */

package com.shanehagan.personalfinance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ccId", nullable = false, unique = true)
    private int ccId;

    @Column(name = "cardName", length = 100, nullable = false)
    private String cardName;

    @Column(name = "purchaseDescription", length = 100, nullable = false)
    private String purchaseDescription;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

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

    public CreditCard() {}

    public CreditCard(String cardName, String purchaseDescription, String category,
                      double amount, LocalDate date, boolean paid, String notes, User user) {
        this.cardName = cardName;
        this.purchaseDescription = purchaseDescription;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.paid = paid;
        this.notes = notes;
        this.user = user;
    }

    public int getCcId() { return ccId; }
    public void setCcId(int ccId) { this.ccId = ccId; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getPurchaseDescription() { return purchaseDescription; }
    public void setPurchaseDescription(String purchaseDescription) { this.purchaseDescription = purchaseDescription; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

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
        CreditCard that = (CreditCard) o;
        return ccId == that.ccId && Double.compare(that.amount, amount) == 0 && paid == that.paid
                && cardName.equals(that.cardName) && purchaseDescription.equals(that.purchaseDescription)
                && Objects.equals(category, that.category) && date.equals(that.date)
                && Objects.equals(notes, that.notes) && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ccId, cardName, purchaseDescription, category, amount, date, paid, notes, user);
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "ccId=" + ccId +
                ", cardName='" + cardName + '\'' +
                ", purchaseDescription='" + purchaseDescription + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", isPaid=" + paid +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                '}';
    }
}
