/**
 * Signup.js
 * Signup page for PersonalFinanceTracker.
 * Submits user registration data to the Spring Boot /signup endpoint.
 * On success, displays a confirmation message and prompts the user to log in.
 */

import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { signup } from '../services/api';

function Signup() {
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', password: '',
    phoneNumber: '', dateOfBirth: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Update form state as user types
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Submit signup request to the backend
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      const response = await signup(formData);
      setSuccess(response.data.message);
    } catch (err) {
      setError(err.response?.data?.error || 'Signup failed. Please try again.');
    }
  };

  return (
    <div className="auth-container">
      <h2>Create Account</h2>
      {error && <div className="alert alert-error">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>First Name</label>
          <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Last Name</label>
          <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Email</label>
          <input type="email" name="email" value={formData.email} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input type="password" name="password" value={formData.password} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Phone Number</label>
          <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Date of Birth</label>
          <input type="text" name="dateOfBirth" placeholder="MM-DD-YYYY" value={formData.dateOfBirth} onChange={handleChange} />
        </div>
        <button type="submit" className="btn btn-primary">Create Account</button>
      </form>
      <div className="auth-link">
        Already have an account? <Link to="/login">Login here</Link>
      </div>
    </div>
  );
}

export default Signup;
