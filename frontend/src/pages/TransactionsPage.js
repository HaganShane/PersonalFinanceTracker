// TransactionsPage.js
// Transactions module page for the Personal Finance Tracker application.
// Displays all income and expense transactions for the logged-in user.
// Allows adding, updating, and deleting transaction entries.
// Includes filtering by type, category, and date range.
// Color codes rows green for income and red for expenses.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import Toast from '../components/Toast';
import ConfirmDialog from '../components/ConfirmDialog';
import '../styles/App.css';

function TransactionsPage() {
  const { uId } = useParams();

  // State to hold the list of transactions for this user
  const [transactions, setTransactions] = useState([]);

  // State to control whether the add/edit form is visible
  const [showForm, setShowForm] = useState(false);

  // State to hold the currently selected transaction for editing (null = new entry)
  const [editTransaction, setEditTransaction] = useState(null);

  // State to hold toast notification message and type
  const [toast, setToast] = useState({ message: '', type: '' });

  // State to control the confirm delete dialog
  const [dialog, setDialog] = useState({ isOpen: false, id: null });

  // State to hold the active filter values
  const [filters, setFilters] = useState({
    type: '', category: '', startDate: '', endDate: ''
  });

  // State to hold the form input values
  const [formData, setFormData] = useState({
    transactionName: '', transactionType: 'Expense',
    category: '', amount: '', date: '', notes: ''
  });

  /**
   * Fetches all transactions for the logged-in user when the component loads
   */
  useEffect(() => {
    fetchTransactions();
  }, [uId]);

  /**
   * Calls the backend to retrieve the full transaction list for this user
   */
  const fetchTransactions = () => {
    api.get(`/transactions/${uId}`).then(res => setTransactions(res.data)).catch(console.error);
  };

  /**
   * Handles filter input change events and updates filter state
   * @param e - the input change event
   */
  const handleFilterChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  /**
   * Applies the selected filters by calling the appropriate backend filter endpoint
   * Falls back to fetching all transactions if no filters are set
   */
  const applyFilters = async () => {
    try {
      if (filters.type) {
        const res = await api.get(`/transactions/${uId}/filterByType?type=${filters.type}`);
        setTransactions(res.data);
      } else if (filters.category) {
        const res = await api.get(`/transactions/${uId}/filterByCategory?category=${filters.category}`);
        setTransactions(res.data);
      } else if (filters.startDate && filters.endDate) {
        const res = await api.get(`/transactions/${uId}/filterByDate?startDate=${filters.startDate}&endDate=${filters.endDate}`);
        setTransactions(res.data);
      } else {
        fetchTransactions();
      }
    } catch (error) {
      setToast({ message: 'Error applying filters. Please try again.', type: 'error' });
    }
  };

  /**
   * Clears all active filters and reloads the full transaction list
   */
  const clearFilters = () => {
    setFilters({ type: '', category: '', startDate: '', endDate: '' });
    fetchTransactions();
  };

  /**
   * Handles input change events and updates form state
   * @param e - the input change event
   */
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  /**
   * Opens the form pre-populated with the selected transaction's data for editing
   * @param transaction - the transaction object to edit
   */
  const handleEdit = (transaction) => {
    setEditTransaction(transaction);
    setFormData({
      transactionName: transaction.transactionName,
      transactionType: transaction.transactionType,
      category: transaction.category || '',
      amount: transaction.amount,
      date: transaction.date,
      notes: transaction.notes || ''
    });
    setShowForm(true);
  };

  /**
   * Handles form submission - either adds a new transaction or updates an existing one
   * Displays a toast notification on success or failure
   * @param e - the form submit event
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editTransaction) {
        await api.put(`/updateTransaction/${uId}/${editTransaction.txnId}`, formData);
        setToast({ message: 'Transaction updated successfully!', type: 'success' });
      } else {
        await api.post(`/addTransaction/${uId}`, formData);
        setToast({ message: 'Transaction added successfully!', type: 'success' });
      }
      resetForm();
      fetchTransactions();
    } catch (error) {
      setToast({ message: 'Error saving transaction. Please try again.', type: 'error' });
    }
  };

  /**
   * Opens the confirm dialog before deleting a transaction
   * @param id - the txnId of the transaction to delete
   */
  const handleDeleteClick = (id) => {
    setDialog({ isOpen: true, id });
  };

  /**
   * Executes the delete after the user confirms in the dialog
   */
  const handleConfirmDelete = async () => {
    try {
      await api.delete(`/deleteTransaction/${uId}/${dialog.id}`);
      setToast({ message: 'Transaction deleted successfully!', type: 'success' });
      fetchTransactions();
    } catch (error) {
      setToast({ message: 'Error deleting transaction. Please try again.', type: 'error' });
    }
    setDialog({ isOpen: false, id: null });
  };

  /**
   * Resets the form and hides it - used after submit or cancel
   */
  const resetForm = () => {
    setFormData({ transactionName: '', transactionType: 'Expense', category: '', amount: '', date: '', notes: '' });
    setEditTransaction(null);
    setShowForm(false);
  };

  return (
      <div className="page-container">
        <Navbar uId={uId} />
        <Toast
            message={toast.message}
            type={toast.type}
            onClose={() => setToast({ message: '', type: '' })}
        />
        <ConfirmDialog
            isOpen={dialog.isOpen}
            message="Are you sure you want to delete this transaction?"
            onConfirm={handleConfirmDelete}
            onCancel={() => setDialog({ isOpen: false, id: null })}
        />
        <div className="module-content">
          <div className="module-header">
            <h2 className="page-title">Transactions</h2>
            <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
              {showForm ? 'Cancel' : '+ Add Transaction'}
            </button>
          </div>

          {/* Filter Bar */}
          <div className="filter-bar">
            <div className="form-group">
              <label>Filter by Type</label>
              <select name="type" value={filters.type} onChange={handleFilterChange}>
                <option value="">All</option>
                <option value="Income">Income</option>
                <option value="Expense">Expense</option>
              </select>
            </div>
            <div className="form-group">
              <label>Filter by Category</label>
              <input
                  type="text"
                  name="category"
                  value={filters.category}
                  onChange={handleFilterChange}
                  placeholder="e.g. Food"
              />
            </div>
            <div className="form-group">
              <label>Start Date</label>
              <input
                  type="date"
                  name="startDate"
                  value={filters.startDate}
                  onChange={handleFilterChange}
              />
            </div>
            <div className="form-group">
              <label>End Date</label>
              <input
                  type="date"
                  name="endDate"
                  value={filters.endDate}
                  onChange={handleFilterChange}
              />
            </div>
            <div className="filter-buttons">
              <button className="btn-primary" onClick={applyFilters}>Apply</button>
              <button className="btn-secondary" onClick={clearFilters}>Clear</button>
            </div>
          </div>

          {/* Add / Edit Form */}
          {showForm && (
              <div className="form-card">
                <h3>{editTransaction ? 'Update Transaction' : 'Add Transaction'}</h3>
                <form onSubmit={handleSubmit} className="module-form">
                  <div className="form-row">
                    <div className="form-group">
                      <label>Transaction Name</label>
                      <input type="text" name="transactionName" value={formData.transactionName} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                      <label>Type</label>
                      <select name="transactionType" value={formData.transactionType} onChange={handleChange}>
                        <option value="Expense">Expense</option>
                        <option value="Income">Income</option>
                      </select>
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group">
                      <label>Category</label>
                      <input type="text" name="category" value={formData.category} onChange={handleChange} />
                    </div>
                    <div className="form-group">
                      <label>Amount ($)</label>
                      <input type="number" step="0.01" name="amount" value={formData.amount} onChange={handleChange} required />
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group">
                      <label>Date</label>
                      <input type="date" name="date" value={formData.date} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                      <label>Notes</label>
                      <input type="text" name="notes" value={formData.notes} onChange={handleChange} />
                    </div>
                  </div>
                  <div className="form-buttons">
                    <button type="submit" className="btn-primary">{editTransaction ? 'Update' : 'Save'}</button>
                    <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
                  </div>
                </form>
              </div>
          )}

          {/* Transactions Table */}
          <div className="table-container">
            <table className="data-table">
              <thead>
              <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Category</th>
                <th>Amount</th>
                <th>Date</th>
                <th>Notes</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tbody>
              {transactions.length === 0 ? (
                  <tr><td colSpan="7" className="no-data">No transactions found. Add one to get started!</td></tr>
              ) : (
                  transactions.map(t => (
                      <tr key={t.txnId} className={t.transactionType === 'Income' ? 'row-income' : 'row-expense'}>
                        <td>{t.transactionName}</td>
                        <td>
                                            <span className={`badge ${t.transactionType === 'Income' ? 'badge-green' : 'badge-red'}`}>
                                                {t.transactionType}
                                            </span>
                        </td>
                        <td>{t.category}</td>
                        <td>${parseFloat(t.amount).toFixed(2)}</td>
                        <td>{t.date}</td>
                        <td>{t.notes}</td>
                        <td>
                          <button className="btn-edit" onClick={() => handleEdit(t)}>Edit</button>
                          <button className="btn-delete" onClick={() => handleDeleteClick(t.txnId)}>Delete</button>
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

export default TransactionsPage;