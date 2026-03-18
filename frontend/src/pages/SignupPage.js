// SignupPage.js
// Signup page for the Personal Finance Tracker application.
// Sends a POST request to the Spring Boot backend to create a new user account.
// On success, displays a confirmation message and redirects to login.
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import '../styles/App.css';

function SignupPage() {
  const navigate = useNavigate();

  // State to hold form input values
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', password: '',
    phoneNumber: '', dateOfBirth: ''
  });

  // State to hold any error or success messages
  const [message, setMessage] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);

  /**
   * Handles input change events and updates form state
   * @param e - the input change event
   */
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  /**
   * Handles form submission - sends new user data to the backend
   * On success, shows a confirmation and redirects to login after a short delay
   * @param e - the form submit event
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/signup', formData);
      setMessage(response.data.message);
      setIsSuccess(true);
      setTimeout(() => navigate('/login'), 2000);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Signup failed. Please try again.');
      setIsSuccess(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2 className="auth-title">Create Account</h2>
        {message && <p className={isSuccess ? 'success-message' : 'error-message'}>{message}</p>}
        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-row">
            <div className="form-group">
              <label>First Name</label>
              <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label>Last Name</label>
              <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
            </div>
          </div>
          <div className="form-group">
            <label>Email</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} required />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label>Phone Number</label>
              <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label>Date of Birth</label>
              <input type="text" name="dateOfBirth" placeholder="MM-DD-YYYY" value={formData.dateOfBirth} onChange={handleChange} />
            </div>
          </div>
          <button type="submit" className="btn-primary full-width">Create Account</button>
        </form>
        <p className="auth-switch">Already have an account? <span onClick={() => navigate('/login')}>Login</span></p>
      </div>
    </div>
  );
}

export default SignupPage;
