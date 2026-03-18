// Navbar.js
// Navigation bar component shared across all module pages.
// Displays links to each module using the uId from the URL to maintain user context.
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/App.css';

/**
 * Navbar component - renders the top navigation bar with module links
 * @param uId - the logged-in user's id, passed as a prop to build navigation URLs
 */
function Navbar({ uId }) {
    const navigate = useNavigate();

    // State to control whether each dropdown menu is open independently
    const [showFinance, setShowFinance] = useState(false);
    const [showWealth, setShowWealth] = useState(false);
    const [showAccount, setShowAccount] = useState(false);

    return (
        <nav className="navbar">
            <div className="navbar-brand" onClick={() => navigate(`/dashboard/${uId}`)}>
                 Personal Finance Tracker
            </div>
            <div className="navbar-links">

                {/* Dashboard - direct link */}
                <span onClick={() => navigate(`/dashboard/${uId}`)}>Dashboard</span>

                {/* Finance dropdown - groups the main tracking modules */}
                <div className="navbar-dropdown"
                     onMouseEnter={() => setShowFinance(true)}
                     onMouseLeave={() => setShowFinance(false)}>
                    <span className="navbar-dropdown-toggle">Finance ▾</span>
                    {showFinance && (
                        <div className="navbar-dropdown-menu">
                            <div onClick={() => navigate(`/transactions/${uId}`)}>Transactions</div>
                            <div onClick={() => navigate(`/budget/${uId}`)}>Budget</div>
                            <div onClick={() => navigate(`/monthlysummary/${uId}`)}>Monthly Summary</div>
                            <div onClick={() => navigate(`/creditcards/${uId}`)}>Credit Cards</div>
                            <div onClick={() => navigate(`/bills/${uId}`)}>Bills</div>
                        </div>
                    )}
                </div>

                {/* Wealth dropdown - groups net worth and savings */}
                <div className="navbar-dropdown"
                     onMouseEnter={() => setShowWealth(true)}
                     onMouseLeave={() => setShowWealth(false)}>
                    <span className="navbar-dropdown-toggle">Wealth ▾</span>
                    {showWealth && (
                        <div className="navbar-dropdown-menu">
                            <div onClick={() => navigate(`/savingsgoals/${uId}`)}>Savings Goals</div>
                            <div onClick={() => navigate(`/networth/${uId}`)}>Net Worth</div>
                        </div>
                    )}
                </div>

                {/* Account dropdown - profile and logout */}
                <div className="navbar-dropdown"
                     onMouseEnter={() => setShowAccount(true)}
                     onMouseLeave={() => setShowAccount(false)}>
                    <span className="navbar-dropdown-toggle">Account ▾</span>
                    {showAccount && (
                        <div className="navbar-dropdown-menu">
                            <div onClick={() => navigate(`/profile/${uId}`)}>My Profile</div>
                            <div className="dropdown-logout" onClick={() => navigate('/')}>Logout</div>
                        </div>
                    )}
                </div>

            </div>
        </nav>
    );
}

export default Navbar;