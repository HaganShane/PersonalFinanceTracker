// NetWorthPage.js
// Net Worth module page for the Personal Finance Tracker application.
// Displays all assets and liabilities for the logged-in user.
// Calculates and displays total assets, total liabilities, and overall net worth.
// Allows adding, updating, and deleting net worth items.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import Toast from '../components/Toast';
import ConfirmDialog from '../components/ConfirmDialog';
import '../styles/App.css';

function NetWorthPage() {
    const { uId } = useParams();

    // State to hold the list of net worth items for this user
    const [items, setItems] = useState([]);

    // State to control whether the add/edit form is visible
    const [showForm, setShowForm] = useState(false);

    // State to hold the currently selected item for editing (null = new entry)
    const [editItem, setEditItem] = useState(null);

    // State to hold toast notification message and type
    const [toast, setToast] = useState({ message: '', type: '' });

    // State to control the confirm delete dialog
    const [dialog, setDialog] = useState({ isOpen: false, id: null });

    // State to hold the form input values
    const [formData, setFormData] = useState({
        itemName: '', itemType: 'Asset', category: '', value: '', notes: ''
    });

    /**
     * Fetches all net worth items for the logged-in user when the component loads
     */
    useEffect(() => {
        fetchItems();
    }, [uId]);

    /**
     * Calls the backend to retrieve the full net worth item list for this user
     */
    const fetchItems = () => {
        api.get(`/networth/${uId}`).then(res => setItems(res.data)).catch(console.error);
    };

    /**
     * Handles input change events and updates form state
     * @param e - the input change event
     */
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    /**
     * Opens the form pre-populated with the selected item's data for editing
     * @param item - the net worth item object to edit
     */
    const handleEdit = (item) => {
        setEditItem(item);
        setFormData({
            itemName: item.itemName,
            itemType: item.itemType,
            category: item.category || '',
            value: item.value,
            notes: item.notes || ''
        });
        setShowForm(true);
    };

    /**
     * Handles form submission - either adds a new item or updates an existing one
     * Displays a toast notification on success or failure
     * @param e - the form submit event
     */
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editItem) {
                await api.put(`/updateNetWorthItem/${uId}/${editItem.nwId}`, formData);
                setToast({ message: 'Item updated successfully!', type: 'success' });
            } else {
                await api.post(`/addNetWorthItem/${uId}`, formData);
                setToast({ message: 'Item added successfully!', type: 'success' });
            }
            resetForm();
            fetchItems();
        } catch (error) {
            setToast({ message: 'Error saving item. Please try again.', type: 'error' });
        }
    };

    /**
     * Opens the confirm dialog before deleting a net worth item
     * @param id - the nwId of the item to delete
     */
    const handleDeleteClick = (id) => {
        setDialog({ isOpen: true, id });
    };

    /**
     * Executes the delete after the user confirms in the dialog
     */
    const handleConfirmDelete = async () => {
        try {
            await api.delete(`/deleteNetWorthItem/${uId}/${dialog.id}`);
            setToast({ message: 'Item deleted successfully!', type: 'success' });
            fetchItems();
        } catch (error) {
            setToast({ message: 'Error deleting item. Please try again.', type: 'error' });
        }
        setDialog({ isOpen: false, id: null });
    };

    /**
     * Resets the form and hides it - used after submit or cancel
     */
    const resetForm = () => {
        setFormData({ itemName: '', itemType: 'Asset', category: '', value: '', notes: '' });
        setEditItem(null);
        setShowForm(false);
    };

    // Calculate totals from the items list
    const totalAssets = items
        .filter(i => i.itemType === 'Asset')
        .reduce((sum, i) => sum + i.value, 0);

    const totalLiabilities = items
        .filter(i => i.itemType === 'Liability')
        .reduce((sum, i) => sum + i.value, 0);

    const netWorth = totalAssets - totalLiabilities;

    return (
        <div className="page-container">
            <Navbar uId={uId} />
            <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: '' })} />
            <ConfirmDialog
                isOpen={dialog.isOpen}
                message="Are you sure you want to delete this item?"
                onConfirm={handleConfirmDelete}
                onCancel={() => setDialog({ isOpen: false, id: null })}
            />
            <div className="module-content">
                <div className="module-header">
                    <h2 className="page-title">Net Worth</h2>
                    <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
                        {showForm ? 'Cancel' : '+ Add Item'}
                    </button>
                </div>

                {/* Net Worth Summary Cards */}
                <div className="networth-summary">
                    <div className="networth-card green">
                        <h3>Total Assets</h3>
                        <p>${totalAssets.toFixed(2)}</p>
                    </div>
                    <div className="networth-card red">
                        <h3>Total Liabilities</h3>
                        <p>${totalLiabilities.toFixed(2)}</p>
                    </div>
                    <div className={`networth-card ${netWorth >= 0 ? 'purple' : 'orange'}`}>
                        <h3>Net Worth</h3>
                        <p>${netWorth.toFixed(2)}</p>
                    </div>
                </div>

                {/* Add / Edit Form */}
                {showForm && (
                    <div className="form-card">
                        <h3>{editItem ? 'Update Item' : 'Add Item'}</h3>
                        <form onSubmit={handleSubmit} className="module-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Item Name</label>
                                    <input type="text" name="itemName" value={formData.itemName} onChange={handleChange} required />
                                </div>
                                <div className="form-group">
                                    <label>Type</label>
                                    <select name="itemType" value={formData.itemType} onChange={handleChange}>
                                        <option value="Asset">Asset</option>
                                        <option value="Liability">Liability</option>
                                    </select>
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Category</label>
                                    <input type="text" name="category" placeholder="e.g. Real Estate, Vehicle, Loan" value={formData.category} onChange={handleChange} />
                                </div>
                                <div className="form-group">
                                    <label>Value ($)</label>
                                    <input type="number" step="0.01" name="value" value={formData.value} onChange={handleChange} required />
                                </div>
                            </div>
                            <div className="form-group">
                                <label>Notes</label>
                                <input type="text" name="notes" value={formData.notes} onChange={handleChange} />
                            </div>
                            <div className="form-buttons">
                                <button type="submit" className="btn-primary">{editItem ? 'Update' : 'Save'}</button>
                                <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
                            </div>
                        </form>
                    </div>
                )}

                {/* Assets Table */}
                <h3 className="section-title">Assets</h3>
                <div className="table-container">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Item</th>
                            <th>Category</th>
                            <th>Value</th>
                            <th>Notes</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {items.filter(i => i.itemType === 'Asset').length === 0 ? (
                            <tr><td colSpan="5" className="no-data">No assets added yet.</td></tr>
                        ) : (
                            items.filter(i => i.itemType === 'Asset').map(i => (
                                <tr key={i.nwId} className="row-income">
                                    <td>{i.itemName}</td>
                                    <td>{i.category}</td>
                                    <td>${parseFloat(i.value).toFixed(2)}</td>
                                    <td>{i.notes}</td>
                                    <td>
                                        <button className="btn-edit" onClick={() => handleEdit(i)}>Edit</button>
                                        <button className="btn-delete" onClick={() => handleDeleteClick(i.nwId)}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>

                {/* Liabilities Table */}
                <h3 className="section-title">Liabilities</h3>
                <div className="table-container">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Item</th>
                            <th>Category</th>
                            <th>Value</th>
                            <th>Notes</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {items.filter(i => i.itemType === 'Liability').length === 0 ? (
                            <tr><td colSpan="5" className="no-data">No liabilities added yet.</td></tr>
                        ) : (
                            items.filter(i => i.itemType === 'Liability').map(i => (
                                <tr key={i.nwId} className="row-expense">
                                    <td>{i.itemName}</td>
                                    <td>{i.category}</td>
                                    <td>${parseFloat(i.value).toFixed(2)}</td>
                                    <td>{i.notes}</td>
                                    <td>
                                        <button className="btn-edit" onClick={() => handleEdit(i)}>Edit</button>
                                        <button className="btn-delete" onClick={() => handleDeleteClick(i.nwId)}>Delete</button>
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

export default NetWorthPage;