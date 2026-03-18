// ConfirmDialog.js
// Reusable confirmation dialog component used across all module pages.
// Displays a modal asking the user to confirm before deleting a record.
// Accepts an isOpen flag, a message, and two callbacks (onConfirm and onCancel) as props.
import React from 'react';
import '../styles/App.css';

/**
 * ConfirmDialog component - renders a modal confirmation dialog
 * @param isOpen - boolean that controls whether the dialog is visible
 * @param message - the confirmation message to display
 * @param onConfirm - callback function to execute if the user confirms
 * @param onCancel - callback function to execute if the user cancels
 */
function ConfirmDialog({ isOpen, message, onConfirm, onCancel }) {

    // If the dialog is not open, render nothing
    if (!isOpen) return null;

    return (
        <div className="dialog-overlay">
            <div className="dialog-box">
                <p className="dialog-message">{message}</p>
                <div className="dialog-buttons">
                    <button className="btn-delete" onClick={onConfirm}>Yes, Delete</button>
                    <button className="btn-secondary" onClick={onCancel}>Cancel</button>
                </div>
            </div>
        </div>
    );
}

export default ConfirmDialog;