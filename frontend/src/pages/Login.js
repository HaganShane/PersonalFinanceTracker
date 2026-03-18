/**
 * Login.js
 * Login page for PersonalFinanceTracker.
 * Submits email/password to the Spring Boot /login endpoint.
 * On success, stores the uId in sessionStorage and redirects to the user's dashboard.
 */

import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../services/api';

function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  // Update form state as user types
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Submit login request to the backend
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await login(formData);
      const { uId } = response.data;
      sessionStorage.setItem('uId', uId);
      navigate(`/dashboard/${uId}`);
    } catch (err) {
      setError(err.response?.data?.error || 'Login failed. Please try again.');
    }
  };

  return (
    <div className="auth-container">
      <h2>Personal Finance Tracker</h2>
      {error && <div className="alert alert-error">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email</label>
          <input type="email" name="email" value={formData.email} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input type="password" name="password" value={formData.password} onChange={handleChange} required />
        </div>
        <button type="submit" className="btn btn-primary">Login</button>
      </form>
      <div className="auth-link">
        Don't have an account? <Link to="/signup">Sign up here</Link>
      </div>
    </div>
  );
}

export default Login;
