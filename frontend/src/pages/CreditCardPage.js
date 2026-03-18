// CreditCardPage.js
// Credit Card module page for the Personal Finance Tracker application.
// Displays all credit card purchases and spending for the logged-in user.
// Allows adding, updating, and deleting credit card entries.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import '../styles/App.css';

function CreditCardPage() {
  const { uId } = useParams();

  const [creditCards, setCreditCards] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editEntry, setEditEntry] = useState(null);
  const [formData, setFormData] = useState({
    cardName: '', purchaseDescription: '', category: '',
    amount: '', date: '', paid: false, notes: ''
  });

  useEffect(() => {
    fetchCreditCards();
  }, [uId]);

  const fetchCreditCards = () => {
    api.get(`/creditcards/${uId}`).then(res => setCreditCards(res.data)).catch(console.error);
  };

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormData({ ...formData, [e.target.name]: value });
  };

  const handleEdit = (entry) => {
    setEditEntry(entry);
    setFormData({
      cardName: entry.cardName, purchaseDescription: entry.purchaseDescription,
      category: entry.category, amount: entry.amount, date: entry.date,
      paid: entry.paid, notes: entry.notes || ''
    });
    setShowForm(true);
  };

  /**
   * Handles form submission - either adds a new credit card entry or updates an existing one
   * @param e - the form submit event
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editEntry) {
        await api.put(`/updateCreditCard/${uId}/${editEntry.ccId}`, formData);
      } else {
        await api.post(`/addCreditCard/${uId}`, formData);
      }
      resetForm();
      fetchCreditCards();
    } catch (error) {
      console.error('Error saving credit card entry:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/deleteCreditCard/${uId}/${id}`);
      fetchCreditCards();
    } catch (error) {
      console.error('Error deleting credit card entry:', error);
    }
  };

  const resetForm = () => {
    setFormData({ cardName: '', purchaseDescription: '', category: '', amount: '', date: '', paid: false, notes: '' });
    setEditEntry(null);
    setShowForm(false);
  };

  return (
    <div className="page-container">
      <Navbar uId={uId} />
      <div className="module-content">
        <div className="module-header">
          <h2 className="page-title">Credit Card Spending</h2>
          <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Cancel' : '+ Add Entry'}
          </button>
        </div>

        {showForm && (
          <div className="form-card">
            <h3>{editEntry ? 'Update Entry' : 'Add Credit Card Entry'}</h3>
            <form onSubmit={handleSubmit} className="module-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Card Name</label>
                  <input type="text" name="cardName" value={formData.cardName} onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Purchase Description</label>
                  <input type="text" name="purchaseDescription" value={formData.purchaseDescription} onChange={handleChange} required />
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
              <div className="form-group checkbox-group">
                <label>
                  <input type="checkbox" name="paid" checked={formData.paid} onChange={handleChange} />
                  &nbsp; Paid
                </label>
              </div>
              <div className="form-buttons">
                <button type="submit" className="btn-primary">{editEntry ? 'Update' : 'Save'}</button>
                <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
              </div>
            </form>
          </div>
        )}

        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Card</th>
                <th>Description</th>
                <th>Category</th>
                <th>Amount</th>
                <th>Date</th>
                <th>Paid</th>
                <th>Notes</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {creditCards.length === 0 ? (
                <tr><td colSpan="8" className="no-data">No credit card entries found. Add one to get started!</td></tr>
              ) : (
                creditCards.map(cc => (
                  <tr key={cc.ccId} className={!cc.paid ? 'row-overdue' : ''}>
                    <td>{cc.cardName}</td>
                    <td>{cc.purchaseDescription}</td>
                    <td>{cc.category}</td>
                    <td>${parseFloat(cc.amount).toFixed(2)}</td>
                    <td>{cc.date}</td>
                    <td><span className={`badge ${cc.paid ? 'badge-green' : 'badge-red'}`}>{cc.paid ? 'Yes' : 'No'}</span></td>
                    <td>{cc.notes}</td>
                    <td>
                      <button className="btn-edit" onClick={() => handleEdit(cc)}>Edit</button>
                      <button className="btn-delete" onClick={() => handleDelete(cc.ccId)}>Delete</button>
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

export default CreditCardPage;
