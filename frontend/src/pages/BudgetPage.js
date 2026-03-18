// BudgetPage.js
// Budget module page for the Personal Finance Tracker application.
// Displays all monthly budget allocations by category for the logged-in user.
// Allows adding, updating, and deleting budget entries.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import '../styles/App.css';

function BudgetPage() {
  const { uId } = useParams();

  // State to hold the list of budgets for this user
  const [budgets, setBudgets] = useState([]);

  // State to control whether the add/edit form is visible
  const [showForm, setShowForm] = useState(false);

  // State to hold the currently selected budget for editing (null = new entry)
  const [editBudget, setEditBudget] = useState(null);

  // State to hold the form input values
  const [formData, setFormData] = useState({
    category: '', budgetedAmount: '', month: '', year: '', notes: ''
  });

  /**
   * Fetches all budgets for the logged-in user when the component loads
   */
  useEffect(() => {
    fetchBudgets();
  }, [uId]);

  const fetchBudgets = () => {
    api.get(`/budgets/${uId}`).then(res => setBudgets(res.data)).catch(console.error);
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleEdit = (budget) => {
    setEditBudget(budget);
    setFormData({
      category: budget.category, budgetedAmount: budget.budgetedAmount,
      month: budget.month, year: budget.year, notes: budget.notes || ''
    });
    setShowForm(true);
  };

  /**
   * Handles form submission - either adds a new budget or updates an existing one
   * @param e - the form submit event
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editBudget) {
        await api.put(`/updateBudget/${uId}/${editBudget.budgetId}`, formData);
      } else {
        await api.post(`/addBudget/${uId}`, formData);
      }
      resetForm();
      fetchBudgets();
    } catch (error) {
      console.error('Error saving budget:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/deleteBudget/${uId}/${id}`);
      fetchBudgets();
    } catch (error) {
      console.error('Error deleting budget:', error);
    }
  };

  const resetForm = () => {
    setFormData({ category: '', budgetedAmount: '', month: '', year: '', notes: '' });
    setEditBudget(null);
    setShowForm(false);
  };

  return (
    <div className="page-container">
      <Navbar uId={uId} />
      <div className="module-content">
        <div className="module-header">
          <h2 className="page-title">Monthly Budgets</h2>
          <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Cancel' : '+ Add Budget'}
          </button>
        </div>

        {showForm && (
          <div className="form-card">
            <h3>{editBudget ? 'Update Budget' : 'Add Budget'}</h3>
            <form onSubmit={handleSubmit} className="module-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Category</label>
                  <input type="text" name="category" value={formData.category} onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Budgeted Amount ($)</label>
                  <input type="number" step="0.01" name="budgetedAmount" value={formData.budgetedAmount} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Month</label>
                  <input type="text" name="month" placeholder="e.g. March" value={formData.month} onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Year</label>
                  <input type="number" name="year" value={formData.year} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-group">
                <label>Notes</label>
                <input type="text" name="notes" value={formData.notes} onChange={handleChange} />
              </div>
              <div className="form-buttons">
                <button type="submit" className="btn-primary">{editBudget ? 'Update' : 'Save'}</button>
                <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
              </div>
            </form>
          </div>
        )}

        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Category</th>
                <th>Budgeted Amount</th>
                <th>Month</th>
                <th>Year</th>
                <th>Notes</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {budgets.length === 0 ? (
                <tr><td colSpan="6" className="no-data">No budgets found. Add one to get started!</td></tr>
              ) : (
                budgets.map(b => (
                  <tr key={b.budgetId}>
                    <td>{b.category}</td>
                    <td>${parseFloat(b.budgetedAmount).toFixed(2)}</td>
                    <td>{b.month}</td>
                    <td>{b.year}</td>
                    <td>{b.notes}</td>
                    <td>
                      <button className="btn-edit" onClick={() => handleEdit(b)}>Edit</button>
                      <button className="btn-delete" onClick={() => handleDelete(b.budgetId)}>Delete</button>
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

export default BudgetPage;
