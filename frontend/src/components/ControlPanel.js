import React, { useState } from 'react';
import './ControlPanel.css';

const ControlPanel = () => {
  const [status, setStatus] = useState(''); // For displaying the system's status
  const [loading, setLoading] = useState(false); // For managing loading states

  // Function to start the system
  const handleStart = async () => {
    setLoading(true); // Indicate loading
    setStatus(''); // Clear previous status
    try {
      const response = await fetch('http://localhost:8080/api/config/start', { method: 'POST' });
      if (response.ok) {
        setStatus('System started successfully.');
      } else {
        const error = await response.text();
        setStatus(`Failed to start: ${error}`);
      }
    } catch (error) {
      setStatus(`Error: ${error.message}`);
    } finally {
      setLoading(false); // Stop loading
    }
  };

  // Function to stop the system
  const handleStop = async () => {
    setLoading(true); // Indicate loading
    setStatus(''); // Clear previous status
    try {
      const response = await fetch('http://localhost:8080/api/config/stop', { method: 'POST' });
      if (response.ok) {
        setStatus('System stopped.');
      } else {
        const error = await response.text();
        setStatus(`Failed to stop: ${error}`);
      }
    } catch (error) {
      setStatus(`Error: ${error.message}`);
    } finally {
      setLoading(false); // Stop loading
    }
  };

  return (
    <div className="control-panel-container">
      <button
        onClick={handleStart}
        className="btn-control start"
        disabled={loading} // Disable the button while loading
      >
        {loading ? 'Starting...' : 'Start'}
      </button>
      <button
        onClick={handleStop}
        className="btn-control stop"
        disabled={loading} // Disable the button while loading
      >
        {loading ? 'Stopping...' : 'Stop'}
      </button>
      {status && <div className="status-message">{status}</div>} {/* Display status */}
    </div>
  );
};

export default ControlPanel;
