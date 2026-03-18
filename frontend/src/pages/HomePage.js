// HomePage.js
// Landing page of the Personal Finance Tracker application.
// Provides navigation to login or signup.
import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/App.css';

function HomePage() {
  const navigate = useNavigate();

  return (
    <div className="home-container">
      <div className="home-card">
        <h1 className="home-title">Personal Finance Tracker</h1>
        <p className="home-subtitle">
          Take control of your finances. Track income, expenses, budgets, credit cards, and savings goals all in one place.
        </p>
        <div className="home-buttons">
          <button className="btn-primary" onClick={() => navigate('/login')}>Login</button>
          <button className="btn-secondary" onClick={() => navigate('/signup')}>Sign Up</button>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
