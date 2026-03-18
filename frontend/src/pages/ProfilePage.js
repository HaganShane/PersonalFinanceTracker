// ProfilePage.js
// Profile page for the Personal Finance Tracker application.
// Displays the logged-in user's current information and allows them to update it.
// Password field is optional - if left blank the existing password is kept unchanged.
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import Navbar from '../components/Navbar';
import Toast from '../components/Toast';
import '../styles/App.css';

function ProfilePage() {
    const { uId } = useParams();

    // State to hold toast notification message and type
    const [toast, setToast] = useState({ message: '', type: '' });

    // State to hold the form input values
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        phoneNumber: '',
        dateOfBirth: ''
    });

    /**
     * Fetches the current user's information when the component loads
     * Pre-populates the form with their existing data
     */
    useEffect(() => {
        api.get(`/user/${uId}`)
            .then(res => {
                const user = res.data;
                setFormData({
                    firstName: user.firstName || '',
                    lastName: user.lastName || '',
                    email: user.email || '',
                    password: '',
                    phoneNumber: user.phoneNumber || '',
                    dateOfBirth: user.dateOfBirth || ''
                });
            })
            .catch(console.error);
    }, [uId]);

    /**
     * Handles input change events and updates form state
     * @param e - the input change event
     */
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    /**
     * Handles form submission - sends updated profile data to the backend
     * Password is only updated if a new one is provided in the form
     * Displays a toast notification on success or failure
     * @param e - the form submit event
     */
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.put(`/updateProfile/${uId}`, formData);
            setToast({ message: 'Profile updated successfully!', type: 'success' });

            // Clear the password field after a successful update
            setFormData(prev => ({ ...prev, password: '' }));
        } catch (error) {
            setToast({ message: 'Error updating profile. Please try again.', type: 'error' });
        }
    };

    return (
        <div className="page-container">
            <Navbar uId={uId} />
            <Toast
                message={toast.message}
                type={toast.type}
                onClose={() => setToast({ message: '', type: '' })}
            />
            <div className="module-content">
                <div className="module-header">
                    <h2 className="page-title">My Profile</h2>
                </div>

                <div className="form-card profile-card">
                    <form onSubmit={handleSubmit} className="module-form">
                        <div className="form-row">
                            <div className="form-group">
                                <label>First Name</label>
                                <input
                                    type="text"
                                    name="firstName"
                                    value={formData.firstName}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>Last Name</label>
                                <input
                                    type="text"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>New Password <span className="optional-label">(leave blank to keep current)</span></label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                placeholder="Enter new password"
                            />
                        </div>
                        <div className="form-row">
                            <div className="form-group">
                                <label>Phone Number</label>
                                <input
                                    type="text"
                                    name="phoneNumber"
                                    value={formData.phoneNumber}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="form-group">
                                <label>Date of Birth</label>
                                <input
                                    type="text"
                                    name="dateOfBirth"
                                    placeholder="MM-DD-YYYY"
                                    value={formData.dateOfBirth}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                        <div className="form-buttons">
                            <button type="submit" className="btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default ProfilePage;