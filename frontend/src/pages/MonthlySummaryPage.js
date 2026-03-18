// MonthlySummaryPage.js
// Monthly Summary module page for the Personal Finance Tracker application.
// Compares budgeted amounts against actual spending from transactions for each category.
// Allows the user to select a month and year to view their budget vs actual breakdown.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import '../styles/App.css';

function MonthlySummaryPage() {
    const { uId } = useParams();

    // State to hold all budgets and transactions for this user
    const [budgets, setBudgets] = useState([]);
    const [transactions, setTransactions] = useState([]);

    // State to hold the selected month and year for filtering
    const [selectedMonth, setSelectedMonth] = useState(
        new Date().toLocaleString('default', { month: 'long' })
    );
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

    // List of months for the dropdown
    const months = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];

    // Generate a range of years for the dropdown
    const years = Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - 2 + i);

    /**
     * Fetches all budgets and transactions for the logged-in user when the component loads
     */
    useEffect(() => {
        api.get(`/budgets/${uId}`).then(res => setBudgets(res.data)).catch(console.error);
        api.get(`/transactions/${uId}`).then(res => setTransactions(res.data)).catch(console.error);
    }, [uId]);

    /**
     * Filters budgets to only those matching the selected month and year
     * Then builds a summary row for each budget category showing budgeted vs actual spending
     * @return - array of summary objects with category, budgeted, actual, and difference
     */
    const buildSummary = () => {
        // Filter budgets to the selected month and year
        const filteredBudgets = budgets.filter(
            b => b.month === selectedMonth && b.year === parseInt(selectedYear)
        );

        // For each budget category, find matching expense transactions in that month/year
        return filteredBudgets.map(budget => {
            const actual = transactions
                .filter(t => {
                    const txnDate = new Date(t.date);
                    const txnMonth = txnDate.toLocaleString('default', { month: 'long' });
                    const txnYear = txnDate.getFullYear();
                    return t.transactionType === 'Expense'
                        && t.category?.toLowerCase() === budget.category?.toLowerCase()
                        && txnMonth === selectedMonth
                        && txnYear === parseInt(selectedYear);
                })
                .reduce((sum, t) => sum + t.amount, 0);

            return {
                category: budget.category,
                budgeted: budget.budgetedAmount,
                actual: actual,
                difference: budget.budgetedAmount - actual
            };
        });
    };

    const summary = buildSummary();

    // Calculate overall totals across all categories
    const totalBudgeted = summary.reduce((sum, s) => sum + s.budgeted, 0);
    const totalActual = summary.reduce((sum, s) => sum + s.actual, 0);
    const totalDifference = totalBudgeted - totalActual;

    return (
        <div className="page-container">
            <Navbar uId={uId} />
            <div className="module-content">
                <div className="module-header">
                    <h2 className="page-title">Monthly Summary</h2>
                </div>

                {/* Month and Year Selectors */}
                <div className="summary-filters">
                    <div className="form-group">
                        <label>Month</label>
                        <select value={selectedMonth} onChange={e => setSelectedMonth(e.target.value)}>
                            {months.map(m => (
                                <option key={m} value={m}>{m}</option>
                            ))}
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Year</label>
                        <select value={selectedYear} onChange={e => setSelectedYear(e.target.value)}>
                            {years.map(y => (
                                <option key={y} value={y}>{y}</option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Summary Totals */}
                <div className="networth-summary">
                    <div className="networth-card blue">
                        <h3>Total Budgeted</h3>
                        <p>${totalBudgeted.toFixed(2)}</p>
                    </div>
                    <div className="networth-card orange">
                        <h3>Total Spent</h3>
                        <p>${totalActual.toFixed(2)}</p>
                    </div>
                    <div className={`networth-card ${totalDifference >= 0 ? 'green' : 'red'}`}>
                        <h3>Remaining</h3>
                        <p>${totalDifference.toFixed(2)}</p>
                    </div>
                </div>

                {/* Budget vs Actual Table */}
                <div className="table-container">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Category</th>
                            <th>Budgeted</th>
                            <th>Actual Spent</th>
                            <th>Difference</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        {summary.length === 0 ? (
                            <tr>
                                <td colSpan="5" className="no-data">
                                    No budgets found for {selectedMonth} {selectedYear}. Add budgets to get started!
                                </td>
                            </tr>
                        ) : (
                            summary.map((s, index) => (
                                <tr key={index} className={s.difference < 0 ? 'row-expense' : ''}>
                                    <td>{s.category}</td>
                                    <td>${s.budgeted.toFixed(2)}</td>
                                    <td>${s.actual.toFixed(2)}</td>
                                    <td>${s.difference.toFixed(2)}</td>
                                    <td>
                                            <span className={`badge ${s.difference >= 0 ? 'badge-green' : 'badge-red'}`}>
                                                {s.difference >= 0 ? 'Under Budget' : 'Over Budget'}
                                            </span>
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default MonthlySummaryPage;