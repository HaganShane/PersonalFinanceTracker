/**
 * @author Shane Hagan
 * Date: 3/16/2025
 * Project: Personal Finance Tracker
 * Class: NetWorth model class - our POJO with variables, table creation, constructors, setters / getters,
 * hashCode & equals methods, and toString method.
 * Tracks individual assets and liabilities for net worth calculation tied to a specific user.
 */

package com.shanehagan.personalfinance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table
public class NetWorth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nwId", nullable = false, unique = true)
    private int nwId;

    @Column(name = "itemName", length = 100, nullable = false)
    private String itemName;

    @Column(name = "itemType", length = 20, nullable = false)
    private String itemType;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "value", nullable = false)
    private double value;

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

    public NetWorth() {}

    public NetWorth(String itemName, String itemType, String category,
                    double value, String notes, User user) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.category = category;
        this.value = value;
        this.notes = notes;
        this.user = user;
    }

    public int getNwId() { return nwId; }
    public void setNwId(int nwId) { this.nwId = nwId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetWorth netWorth = (NetWorth) o;
        return nwId == netWorth.nwId && Double.compare(netWorth.value, value) == 0
                && itemName.equals(netWorth.itemName) && itemType.equals(netWorth.itemType)
                && Objects.equals(category, netWorth.category)
                && Objects.equals(notes, netWorth.notes)
                && user.equals(netWorth.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nwId, itemName, itemType, category, value, notes, user);
    }

    @Override
    public String toString() {
        return "NetWorth{" +
                "nwId=" + nwId +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                '}';
    }
}