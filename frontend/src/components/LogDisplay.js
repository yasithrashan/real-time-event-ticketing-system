import React, { useEffect, useState } from 'react';
import './LogDisplay.css'; // Import the stylesheet

const LogDisplay = () => {
  const [logs, setLogs] = useState([]); // State to hold logs

  useEffect(() => {
    // Establish an SSE connection to the backend
    const eventSource = new EventSource('http://localhost:8080/api/logs/stream');

    // Listen for incoming messages from the server
    eventSource.onmessage = (event) => {
      setLogs((prevLogs) => [...prevLogs, event.data]); // Add the new log to the list
    };

    // Handle errors (e.g., disconnects)
    eventSource.onerror = (error) => {
      console.error('Error in SSE connection:', error);
      eventSource.close(); // Close the connection
    };

    // Clean up the SSE connection when the component unmounts
    return () => {
      eventSource.close();
    };
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
