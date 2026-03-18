/**
 * @author Shane Hagan
 * Date: 3/16/2025
 * Project: Personal Finance Tracker
 * Class: User model class - our POJO with variables, table creation, constructors, setters / getters,
 * hashCode & equals methods, and toString method.
 * Also mapping the OneToMany to each of our other models for joined tables to pull our data from.
 */

package com.shanehagan.personalfinance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uId", nullable = false, unique = true)
    private int uId;

    @Column(name = "firstName", length = 50, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phoneNumber", length = 12)
    private String phoneNumber;

    @Column(name = "dateOfBirth")
    private String dateOfBirth;

    /**
     * Changed from ManyToOne to OneToOne, pointing back to the transaction class
     * Assisted in putting the userId in the model table to track which user inputs a given record.
     * Useful when grabbing all records by a userId
     * Mapped by the user
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = Transaction.class, cascade = CascadeType.ALL)
    private List<Transaction> userTransactions;

    /**
     * Changed from ManyToOne to OneToOne, pointing back to the budget class
     * Assisted in putting the userId in the model table to track which user inputs a given record.
     * Useful when grabbing all records by a userId
     * Mapped by the user
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = Budget.class, cascade = CascadeType.ALL)
    private List<Budget> userBudgets;

    /**
     * Changed from ManyToOne to OneToOne, pointing back to the credit card class
     * Assisted in putting the userId in the model table to track which user inputs a given record.
     * Useful when grabbing all records by a userId
     * Mapped by the user
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = CreditCard.class, cascade = CascadeType.ALL)
    private List<CreditCard> userCreditCards;

    /**
     * Changed from ManyToOne to OneToOne, pointing back to the savings goal class
     * Assisted in putting the userId in the model table to track which user inputs a given record.
     * Useful when grabbing all records by a userId
     * Mapped by the user
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = SavingsGoal.class, cascade = CascadeType.ALL)
    private List<SavingsGoal> userSavingsGoals;

    /**
     * Changed from ManyToOne to OneToOne, pointing back to the bill class
     * Assisted in putting the userId in the model table to track which user inputs a given record.
     * Useful when grabbing all records by a userId
     * Mapped by the user
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = Bill.class, cascade = CascadeType.ALL)
    private List<Bill> userBills;

    /**
     * Changed from ManyToOne to OneToOne, pointing back to the net worth class
     * Assisted in putting the userId in the model table to track which user inputs a given record.
     * Useful when grabbing all records by a userId
     * Mapped by the user
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = NetWorth.class, cascade = CascadeType.ALL)
    private List<NetWorth> userNetWorthItems;

    public User() {}

    public User(String firstName, String lastName, String email, String phoneNumber, String dateOfBirth,
                List<Transaction> userTransactions, List<Budget> userBudgets,
                List<CreditCard> userCreditCards, List<SavingsGoal> userSavingsGoals) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.userTransactions = userTransactions;
        this.userBudgets = userBudgets;
        this.userCreditCards = userCreditCards;
        this.userSavingsGoals = userSavingsGoals;
    }

    public int getuId() { return uId; }
    public void setuId(int uId) { this.uId = uId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public List<Transaction> getUserTransactions() { return userTransactions; }
    public void setUserTransactions(List<Transaction> userTransactions) { this.userTransactions = userTransactions; }

    public List<Budget> getUserBudgets() { return userBudgets; }
    public void setUserBudgets(List<Budget> userBudgets) { this.userBudgets = userBudgets; }

    public List<CreditCard> getUserCreditCards() { return userCreditCards; }
    public void setUserCreditCards(List<CreditCard> userCreditCards) { this.userCreditCards = userCreditCards; }

    public List<SavingsGoal> getUserSavingsGoals() { return userSavingsGoals; }
    public void setUserSavingsGoals(List<SavingsGoal> userSavingsGoals) { this.userSavingsGoals = userSavingsGoals; }

    public List<Bill> getUserBills() { return userBills; }
    public void setUserBills(List<Bill> userBills) { this.userBills = userBills; }

    public List<NetWorth> getUserNetWorthItems() { return userNetWorthItems; }
    public void setUserNetWorthItems(List<NetWorth> userNetWorthItems) { this.userNetWorthItems = userNetWorthItems; }

    @Override
    public int hashCode() {
        return Objects.hash(uId, firstName, lastName, email, password, phoneNumber, dateOfBirth,
                userTransactions, userBudgets, userCreditCards, userSavingsGoals);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return uId == other.uId && Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName) && Objects.equals(email, other.email)
                && Objects.equals(password, other.password) && Objects.equals(phoneNumber, other.phoneNumber)
                && Objects.equals(dateOfBirth, other.dateOfBirth)
                && Objects.equals(userTransactions, other.userTransactions)
                && Objects.equals(userBudgets, other.userBudgets)
                && Objects.equals(userCreditCards, other.userCreditCards)
                && Objects.equals(userSavingsGoals, other.userSavingsGoals);
    }

    @Override
    public String toString() {
        return "User [uId=" + uId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", password=" + password + ", phoneNumber=" + phoneNumber + ", dateOfBirth=" + dateOfBirth
                + ", userTransactions=" + userTransactions + ", userBudgets=" + userBudgets
                + ", userCreditCards=" + userCreditCards + ", userSavingsGoals=" + userSavingsGoals + "]";
    }
}
