// App.js
// Root component - sets up React Router and defines all application routes.
// Routes mirror the module structure of the Spring Boot backend.
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import DashboardPage from './pages/DashboardPage';
import TransactionsPage from './pages/TransactionsPage';
import BudgetPage from './pages/BudgetPage';
import CreditCardPage from './pages/CreditCardPage';
import SavingsGoalPage from './pages/SavingsGoalPage';
import BillsPage from './pages/BillsPage';
import NetWorthPage from './pages/NetWorthPage';
import MonthlySummaryPage from './pages/MonthlySummaryPage';
import ProfilePage from './pages/ProfilePage';

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/dashboard/:uId" element={<DashboardPage />} />
          <Route path="/transactions/:uId" element={<TransactionsPage />} />
          <Route path="/budget/:uId" element={<BudgetPage />} />
          <Route path="/creditcards/:uId" element={<CreditCardPage />} />
          <Route path="/savingsgoals/:uId" element={<SavingsGoalPage />} />
          <Route path="/bills/:uId" element={<BillsPage />} />
          <Route path="/networth/:uId" element={<NetWorthPage />} />
          <Route path="/monthlysummary/:uId" element={<MonthlySummaryPage />} />
          <Route path="/profile/:uId" element={<ProfilePage />} />
        </Routes>
      </Router>
  );
}

export default App;