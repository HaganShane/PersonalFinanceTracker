// DashboardPage.js
// Dashboard page for the Personal Finance Tracker application.
// Displays a summary overview for the logged-in user and navigation to each module.
// Fetches recent data from each module to display summary cards on load.
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import '../styles/App.css';

function DashboardPage() {
  const { uId } = useParams();
  const navigate = useNavigate();

  // State to hold summary data fetched from the backend
  const [user, setUser] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [budgets, setBudgets] = useState([]);
  const [creditCards, setCreditCards] = useState([]);
  const [savingsGoals, setSavingsGoals] = useState([]);

  /**
   * Fetches all module data for the logged-in user when the component loads
   * Uses the uId from the URL to target the correct user's data
   */
  useEffect(() => {
    api.get(`/user/${uId}`).then(res => setUser(res.data)).catch(console.error);
    api.get(`/transactions/${uId}`).then(res => setTransactions(res.data)).catch(console.error);
    api.get(`/budgets/${uId}`).then(res => setBudgets(res.data)).catch(console.error);
    api.get(`/creditcards/${uId}`).then(res => setCreditCards(res.data)).catch(console.error);
    api.get(`/savingsgoals/${uId}`).then(res => setSavingsGoals(res.data)).catch(console.error);
  }, [uId]);

  // Calculate total income and expenses from transactions
  const totalIncome = transactions.filter(t => t.transactionType === 'Income').reduce((sum, t) => sum + t.amount, 0);
  const totalExpenses = transactions.filter(t => t.transactionType === 'Expense').reduce((sum, t) => sum + t.amount, 0);

  // Calculate total credit card spending
  const totalCCSpending = creditCards.reduce((sum, cc) => sum + cc.amount, 0);

  // Calculate overall savings progress
  const totalSaved = savingsGoals.reduce((sum, g) => sum + g.currentAmount, 0);
  const totalTarget = savingsGoals.reduce((sum, g) => sum + g.targetAmount, 0);

  return (
    <div className="page-container">
      <Navbar uId={uId} />
      <div className="dashboard-content">
        <h2 className="page-title">
          Welcome back, {user ? user.firstName : ''}!
        </h2>

        {/* Summary Cards */}
        <div className="summary-grid">
          <div className="summary-card green" onClick={() => navigate(`/transactions/${uId}`)}>
            <h3>Transactions</h3>
            <p className="summary-number">{transactions.length} entries</p>
            <p>Income: ${totalIncome.toFixed(2)}</p>
            <p>Expenses: ${totalExpenses.toFixed(2)}</p>
          </div>
          <div className="summary-card blue" onClick={() => navigate(`/budget/${uId}`)}>
            <h3>Budgets</h3>
            <p className="summary-number">{budgets.length} categories</p>
            <p>Total Budgeted: ${budgets.reduce((sum, b) => sum + b.budgetedAmount, 0).toFixed(2)}</p>
          </div>
          <div className="summary-card orange" onClick={() => navigate(`/creditcards/${uId}`)}>
            <h3>Credit Cards</h3>
            <p className="summary-number">{creditCards.length} entries</p>
            <p>Total Spending: ${totalCCSpending.toFixed(2)}</p>
          </div>
          <div className="summary-card purple" onClick={() => navigate(`/savingsgoals/${uId}`)}>
            <h3>Savings Goals</h3>
            <p className="summary-number">{savingsGoals.length} goals</p>
            <p>Saved: ${totalSaved.toFixed(2)} / ${totalTarget.toFixed(2)}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DashboardPage;
