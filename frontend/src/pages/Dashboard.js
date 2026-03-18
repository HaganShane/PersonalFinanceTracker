/**
 * Dashboard.js
 * Main dashboard for PersonalFinanceTracker.
 * Displays a summary of the user's financial data across all four modules.
 * Fetches counts and totals for transactions, budgets, credit cards, and savings goals.
 */

import React, { useEffect, useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { getTransactions, getBudgets, getCreditCards, getSavingsGoals } from '../services/api';

function Dashboard() {
  const { uId } = useParams();
  const navigate = useNavigate();

  const [summary, setSummary] = useState({
    totalIncome: 0,
    totalExpenses: 0,
    budgetCount: 0,
    creditCardTotal: 0,
    savingsGoalCount: 0
  });

  // Fetch data from all modules to populate dashboard summary cards
  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const [txns, budgets, cc, goals] = await Promise.all([
          getTransactions(uId),
          getBudgets(uId),
          getCreditCards(uId),
          getSavingsGoals(uId)
        ]);

        const income = txns.data
          .filter(t => t.txnType === 'Income')
          .reduce((sum, t) => sum + t.amount, 0);
        const expenses = txns.data
          .filter(t => t.txnType === 'Expense')
          .reduce((sum, t) => sum + t.amount, 0);
        const ccTotal = cc.data.reduce((sum, c) => sum + c.amount, 0);

        setSummary({
          totalIncome: income.toFixed(2),
          totalExpenses: expenses.toFixed(2),
          budgetCount: budgets.data.length,
          creditCardTotal: ccTotal.toFixed(2),
          savingsGoalCount: goals.data.length
        });
      } catch (err) {
        console.error('Error loading dashboard data:', err);
      }
    };

    fetchSummary();
  }, [uId]);

  // Clear session and redirect to login
  const handleLogout = () => {
    sessionStorage.clear();
    navigate('/login');
  };

  return (
    <div>
      <nav className="navbar">
        <h2>💰 Personal Finance Tracker</h2>
        <div>
          <Link to={`/transactions/${uId}`}>Transactions</Link>
          <Link to={`/budgets/${uId}`}>Budgets</Link>
          <Link to={`/creditcards/${uId}`}>Credit Cards</Link>
          <Link to={`/savingsgoals/${uId}`}>Savings Goals</Link>
          <button onClick={handleLogout} className="btn btn-secondary" style={{ marginLeft: '20px' }}>Logout</button>
        </div>
      </nav>

      <div className="page-container">
        <h2 style={{ marginBottom: '20px', color: '#1a3c5e' }}>Dashboard</h2>

        <div className="summary-grid">
          <div className="summary-card">
            <h4>Total Income</h4>
            <p>${summary.totalIncome}</p>
          </div>
          <div className="summary-card">
            <h4>Total Expenses</h4>
            <p>${summary.totalExpenses}</p>
          </div>
          <div className="summary-card">
            <h4>Active Budgets</h4>
            <p>{summary.budgetCount}</p>
          </div>
          <div className="summary-card">
            <h4>Credit Card Spend</h4>
            <p>${summary.creditCardTotal}</p>
          </div>
          <div className="summary-card">
            <h4>Savings Goals</h4>
            <p>{summary.savingsGoalCount}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
