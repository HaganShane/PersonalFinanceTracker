// Toast.js
// Reusable toast notification component used across all module pages.
// Displays a temporary success or error message that automatically disappears after 3 seconds.
// Accepts a message string and a type (success or error) as props.
import React, { useEffect } from 'react';
import '../styles/App.css';

/**
 * Toast component - renders a temporary notification message
 * @param message - the text to display in the toast
 * @param type - either 'success' or 'error' to control the color
 * @param onClose - callback function to clear the toast from the parent state
 */
function Toast({ message, type, onClose }) {

    /**
     * Automatically dismisses the toast after 3 seconds
     * Calls the onClose callback to clear the message from the parent component's state
     */
    useEffect(() => {
        const timer = setTimeout(() => {
            onClose();
        }, 3000);

        // Cleanup the timer if the component unmounts before the 3 seconds is up
        return () => clearTimeout(timer);
    }, [onClose]);

    if (!message) return null;

    return (
        <div className={`toast toast-${type}`}>
            <span>{message}</span>
            <button className="toast-close" onClick={onClose}>✕</button>
        </div>
    );
}

export default Toast;