import React, { useEffect, useState } from 'react';
import './LogDisplay.css'; // Import the stylesheet

const LogDisplay = () => {
  const [logs, setLogs] = useState([]); // State to hold logs

  useEffect(() => {
    // Function to fetch logs from the backend
    const fetchLogs = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/logs'); // Replace with your polling endpoint
        if (response.ok) {
          const data = await response.json();
          setLogs(data); // Update the logs state with the fetched data
        } else {
          console.error('Failed to fetch logs:', response.status);
        }
      } catch (error) {
        console.error('Error while fetching logs:', error);
      }
    };

    // Fetch logs initially and set up periodic polling
    fetchLogs();
    const interval = setInterval(fetchLogs, 2000); // Poll every 2 seconds

    // Cleanup the polling interval when the component unmounts
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="log-display">
      <h2>System Logs</h2>
      <div className="log-container">
        <ul className="log-list">
          {logs.map((log, index) => (
            <li key={index}>{log}</li> // Display each log
          ))}
        </ul>
      </div>
    </div>
  );
};

export default LogDisplay;
