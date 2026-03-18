// SavingsGoalPage.js
// Savings Goal module page for the Personal Finance Tracker application.
// Displays all savings goals and progress for the logged-in user.
// Allows adding, updating, and deleting savings goal entries.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import '../styles/App.css';

function SavingsGoalPage() {
  const { uId } = useParams();

  const [savingsGoals, setSavingsGoals] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editGoal, setEditGoal] = useState(null);
  const [formData, setFormData] = useState({
    goalName: '', targetAmount: '', currentAmount: '', targetDate: '', notes: ''
  });

  useEffect(() => {
    fetchSavingsGoals();
  }, [uId]);

  const fetchSavingsGoals = () => {
    api.get(`/savingsgoals/${uId}`).then(res => setSavingsGoals(res.data)).catch(console.error);
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleEdit = (goal) => {
    setEditGoal(goal);
    setFormData({
      goalName: goal.goalName, targetAmount: goal.targetAmount,
      currentAmount: goal.currentAmount, targetDate: goal.targetDate || '', notes: goal.notes || ''
    });
    setShowForm(true);
  };

  /**
   * Handles form submission - either adds a new savings goal or updates an existing one
   * @param e - the form submit event
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editGoal) {
        await api.put(`/updateSavingsGoal/${uId}/${editGoal.goalId}`, formData);
      } else {
        await api.post(`/addSavingsGoal/${uId}`, formData);
      }
      resetForm();
      fetchSavingsGoals();
    } catch (error) {
      console.error('Error saving savings goal:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/deleteSavingsGoal/${uId}/${id}`);
      fetchSavingsGoals();
    } catch (error) {
      console.error('Error deleting savings goal:', error);
    }
  };

  const resetForm = () => {
    setFormData({ goalName: '', targetAmount: '', currentAmount: '', targetDate: '', notes: '' });
    setEditGoal(null);
    setShowForm(false);
  };

  return (
    <div className="page-container">
      <Navbar uId={uId} />
      <div className="module-content">
        <div className="module-header">
          <h2 className="page-title">Savings Goals</h2>
          <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Cancel' : '+ Add Goal'}
          </button>
        </div>

        {showForm && (
          <div className="form-card">
            <h3>{editGoal ? 'Update Goal' : 'Add Savings Goal'}</h3>
            <form onSubmit={handleSubmit} className="module-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Goal Name</label>
                  <input type="text" name="goalName" value={formData.goalName} onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Target Amount ($)</label>
                  <input type="number" step="0.01" name="targetAmount" value={formData.targetAmount} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Current Amount ($)</label>
                  <input type="number" step="0.01" name="currentAmount" value={formData.currentAmount} onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Target Date</label>
                  <input type="date" name="targetDate" value={formData.targetDate} onChange={handleChange} />
                </div>
              </div>
              <div className="form-group">
                <label>Notes</label>
                <input type="text" name="notes" value={formData.notes} onChange={handleChange} />
              </div>
              <div className="form-buttons">
                <button type="submit" className="btn-primary">{editGoal ? 'Update' : 'Save'}</button>
                <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
              </div>
            </form>
          </div>
        )}

        {/* Goals with progress bars */}
        <div className="goals-grid">
          {savingsGoals.length === 0 ? (
            <p className="no-data">No savings goals found. Add one to get started!</p>
          ) : (
            savingsGoals.map(g => {
              const progress = Math.min((g.currentAmount / g.targetAmount) * 100, 100);
              return (
                <div key={g.goalId} className="goal-card">
                  <div className="goal-card-header">
                    <h3>{g.goalName}</h3>
                    <div>
                      <button className="btn-edit" onClick={() => handleEdit(g)}>Edit</button>
                      <button className="btn-delete" onClick={() => handleDelete(g.goalId)}>Delete</button>
                    </div>
                  </div>
                  <p>${parseFloat(g.currentAmount).toFixed(2)} saved of ${parseFloat(g.targetAmount).toFixed(2)}</p>
                  <div className="progress-bar-container">
                    <div className="progress-bar" style={{ width: `${progress}%` }}></div>
                  </div>
                  <p className="progress-label">{progress.toFixed(1)}% complete</p>
                  {g.targetDate && <p className="goal-date">Target: {g.targetDate}</p>}
                  {g.notes && <p className="goal-notes">{g.notes}</p>}
                </div>
              );
            })
          )}
        </div>
      </div>
    </div>
  );
}

export default SavingsGoalPage;
