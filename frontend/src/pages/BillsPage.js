// BillsPage.js
// Bills module page for the Personal Finance Tracker application.
// Displays all recurring bills and their due dates for the logged-in user.
// Highlights overdue bills in red and bills due within 7 days in yellow.
// Allows adding, updating, and deleting bill entries.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import Toast from '../components/Toast';
import ConfirmDialog from '../components/ConfirmDialog';
import '../styles/App.css';

function BillsPage() {
    const { uId } = useParams();

    // State to hold the list of bills for this user
    const [bills, setBills] = useState([]);

    // State to control whether the add/edit form is visible
    const [showForm, setShowForm] = useState(false);

    // State to hold the currently selected bill for editing (null = new entry)
    const [editBill, setEditBill] = useState(null);

    // State to hold toast notification message and type
    const [toast, setToast] = useState({ message: '', type: '' });

    // State to control the confirm delete dialog
    const [dialog, setDialog] = useState({ isOpen: false, id: null });

    // State to hold the form input values
    const [formData, setFormData] = useState({
        billName: '', category: '', amount: '',
        dueDate: '', recurring: false, paid: false, notes: ''
    });

    /**
     * Fetches all bills for the logged-in user when the component loads
     */
    useEffect(() => {
        fetchBills();
    }, [uId]);

    /**
     * Calls the backend to retrieve the full bill list for this user
     */
    const fetchBills = () => {
        api.get(`/bills/${uId}`).then(res => setBills(res.data)).catch(console.error);
    };

    /**
     * Handles input change events and updates form state
     * Handles both text inputs and checkboxes
     * @param e - the input change event
     */
    const handleChange = (e) => {
        const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
        setFormData({ ...formData, [e.target.name]: value });
    };

    /**
     * Opens the form pre-populated with the selected bill's data for editing
     * @param bill - the bill object to edit
     */
    const handleEdit = (bill) => {
        setEditBill(bill);
        setFormData({
            billName: bill.billName,
            category: bill.category || '',
            amount: bill.amount,
            dueDate: bill.dueDate,
            recurring: bill.recurring,
            paid: bill.paid,
            notes: bill.notes || ''
        });
        setShowForm(true);
    };

    /**
     * Handles form submission - either adds a new bill or updates an existing one
     * Displays a toast notification on success or failure
     * @param e - the form submit event
     */
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editBill) {
                await api.put(`/updateBill/${uId}/${editBill.billId}`, formData);
                setToast({ message: 'Bill updated successfully!', type: 'success' });
            } else {
                await api.post(`/addBill/${uId}`, formData);
                setToast({ message: 'Bill added successfully!', type: 'success' });
            }
            resetForm();
            fetchBills();
        } catch (error) {
            setToast({ message: 'Error saving bill. Please try again.', type: 'error' });
        }
    };

    /**
     * Opens the confirm dialog before deleting a bill
     * @param id - the billId of the bill to delete
     */
    const handleDeleteClick = (id) => {
        setDialog({ isOpen: true, id });
    };

    /**
     * Executes the delete after the user confirms in the dialog
     */
    const handleConfirmDelete = async () => {
        try {
            await api.delete(`/deleteBill/${uId}/${dialog.id}`);
            setToast({ message: 'Bill deleted successfully!', type: 'success' });
            fetchBills();
        } catch (error) {
            setToast({ message: 'Error deleting bill. Please try again.', type: 'error' });
        }
        setDialog({ isOpen: false, id: null });
    };

    /**
     * Resets the form and hides it - used after submit or cancel
     */
    const resetForm = () => {
        setFormData({ billName: '', category: '', amount: '', dueDate: '', recurring: false, paid: false, notes: '' });
        setEditBill(null);
        setShowForm(false);
    };

    /**
     * Determines the CSS class for a bill row based on its due date and paid status
     * Overdue unpaid bills get red highlighting, bills due within 7 days get yellow
     * @param bill - the bill object to evaluate
     * @return - a CSS class name string
     */
    const getRowClass = (bill) => {
        if (bill.paid) return '';
        const today = new Date();
        const due = new Date(bill.dueDate);
        const daysUntilDue = (due - today) / (1000 * 60 * 60 * 24);
        if (daysUntilDue < 0) return 'row-overdue';
        if (daysUntilDue <= 7) return 'row-warning';
        return '';
    };

    return (
        <div className="page-container">
            <Navbar uId={uId} />
            <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: '' })} />
            <ConfirmDialog
                isOpen={dialog.isOpen}
                message="Are you sure you want to delete this bill?"
                onConfirm={handleConfirmDelete}
                onCancel={() => setDialog({ isOpen: false, id: null })}
            />
            <div className="module-content">
                <div className="module-header">
                    <h2 className="page-title">Bills</h2>
                    <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
                        {showForm ? 'Cancel' : '+ Add Bill'}
                    </button>
                </div>

                {/* Add / Edit Form */}
                {showForm && (
                    <div className="form-card">
                        <h3>{editBill ? 'Update Bill' : 'Add Bill'}</h3>
                        <form onSubmit={handleSubmit} className="module-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Bill Name</label>
                                    <input type="text" name="billName" value={formData.billName} onChange={handleChange} required />
                                </div>
                                <div className="form-group">
                                    <label>Category</label>
                                    <input type="text" name="category" value={formData.category} onChange={handleChange} />
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Amount ($)</label>
                                    <input type="number" step="0.01" name="amount" value={formData.amount} onChange={handleChange} required />
                                </div>
                                <div className="form-group">
                                    <label>Due Date</label>
                                    <input type="date" name="dueDate" value={formData.dueDate} onChange={handleChange} required />
                                </div>
                            </div>
                            <div className="form-group">
                                <label>Notes</label>
                                <input type="text" name="notes" value={formData.notes} onChange={handleChange} />
                            </div>
                            <div className="form-row">
                                <div className="form-group checkbox-group">
                                    <label>
                                        <input type="checkbox" name="recurring" checked={formData.recurring} onChange={handleChange} />
                                        &nbsp; Recurring
                                    </label>
                                </div>
                                <div className="form-group checkbox-group">
                                    <label>
                                        <input type="checkbox" name="paid" checked={formData.paid} onChange={handleChange} />
                                        &nbsp; Paid
                                    </label>
                                </div>
                            </div>
                            <div className="form-buttons">
                                <button type="submit" className="btn-primary">{editBill ? 'Update' : 'Save'}</button>
                                <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
                            </div>
                        </form>
                    </div>
                )}

                {/* Bills Table */}
                <div className="table-container">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Bill Name</th>
                            <th>Category</th>
                            <th>Amount</th>
                            <th>Due Date</th>
                            <th>Recurring</th>
                            <th>Paid</th>
                            <th>Notes</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {bills.length === 0 ? (
                            <tr><td colSpan="8" className="no-data">No bills found. Add one to get started!</td></tr>
                        ) : (
                            bills.map(b => (
                                <tr key={b.billId} className={getRowClass(b)}>
                                    <td>{b.billName}</td>
                                    <td>{b.category}</td>
                                    <td>${parseFloat(b.amount).toFixed(2)}</td>
                                    <td>{b.dueDate}</td>
                                    <td><span className={`badge ${b.recurring ? 'badge-blue' : 'badge-gray'}`}>{b.recurring ? 'Yes' : 'No'}</span></td>
                                    <td><span className={`badge ${b.paid ? 'badge-green' : 'badge-red'}`}>{b.paid ? 'Yes' : 'No'}</span></td>
                                    <td>{b.notes}</td>
                                    <td>
                                        <button className="btn-edit" onClick={() => handleEdit(b)}>Edit</button>
                                        <button className="btn-delete" onClick={() => handleDeleteClick(b.billId)}>Delete</button>
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

export default BillsPage;