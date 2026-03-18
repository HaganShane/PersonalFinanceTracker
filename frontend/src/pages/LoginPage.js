// LoginPage.js
// Login page for the Personal Finance Tracker application.
// Sends a POST request to the Spring Boot backend to authenticate the user.
// On success, redirects to the user's dashboard using their uId.
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import '../styles/App.css';

function LoginPage() {
  const navigate = useNavigate();

  // State to hold form input values
  const [formData, setFormData] = useState({ email: '', password: '' });

  // State to hold any error or success messages
  const [message, setMessage] = useState('');

  /**
   * Handles input change events and updates form state
   * @param e - the input change event
   */
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  /**
   * Handles form submission - sends login credentials to the backend
   * On success, redirects to the user dashboard with their uId in the URL
   * @param e - the form submit event
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/login', formData);
      const { uId } = response.data;
      navigate(`/dashboard/${uId}`);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Login failed. Please try again.');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2 className="auth-title">Login</h2>
        {message && <p className="error-message">{message}</p>}
        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label>Email</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} required />
          </div>
          <button type="submit" className="btn-primary full-width">Login</button>
        </form>
        <p className="auth-switch">Don't have an account? <span onClick={() => navigate('/signup')}>Sign up</span></p>
      </div>
    </div>
  );
}

export default LoginPage;
