
// export default ConfigurationForm;

import React, { useState } from 'react';
import './ConfigurationForm.css';

const ConfigurationForm = () => {
  const [formData, setFormData] = useState({
    totalTickets: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: '',
  });

  const [status, setStatus] = useState('');
  const [loading, setLoading] = useState(false);

  // Handle form input changes
  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [id]: value,
    }));
  };

  // Handle form submission (POST)
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setStatus('');

    try {
      const response = await fetch('http://localhost:8080/api/config', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setStatus('Configuration successfully updated!');
      } else {
        const error = await response.text();
        setStatus(`Failed to update configuration: ${error}`);
      }
    } catch (error) {
      setStatus(`Error: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  // Handle fetching the configuration (GET)
  const handleGetConfiguration = async () => {
    setLoading(true);
    setStatus('');

    try {
      const response = await fetch('http://localhost:8080/api/config', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        const data = await response.json();
        setFormData({
          totalTickets: data.totalTickets || '',
          ticketReleaseRate: data.ticketReleaseRate || '',
          customerRetrievalRate: data.customerRetrievalRate || '',
          maxTicketCapacity: data.maxTicketCapacity || '',
        });
        setStatus('Configuration retrieved successfully!');
      } else {
        const error = await response.text();
        setStatus(`Failed to retrieve configuration: ${error}`);
      }
    } catch (error) {
      setStatus(`Error: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="configuration-form-container">
      <h2 className="configuration-title">Configuration Settings</h2>
      <form className="configuration-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="totalTickets">Total Tickets</label>
          <input
            type="text"
            id="totalTickets"
            className="input-text"
            placeholder="Enter total tickets"
            value={formData.totalTickets}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="ticketReleaseRate">Ticket Release Rate</label>
          <input
            type="text"
            id="ticketReleaseRate"
            className="input-text"
            placeholder="Enter release rate"
            value={formData.ticketReleaseRate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="customerRetrievalRate">Customer Retrieval Rate</label>
          <input
            type="text"
            id="customerRetrievalRate"
            className="input-text"
            placeholder="Enter retrieval rate"
            value={formData.customerRetrievalRate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="maxTicketCapacity">Max Ticket Capacity</label>
          <input
            type="text"
            id="maxTicketCapacity"
            className="input-text"
            placeholder="Enter max capacity"
            value={formData.maxTicketCapacity}
            onChange={handleChange}
            required
          />
        </div>
        {/* Buttons for POST and GET */}
        <div className="button-group">
          <button type="submit" className="btn-save" disabled={loading}>
            {loading ? 'Saving...' : 'Save Configuration'}
          </button>
          <button
            type="button"
            className="btn-get"
            onClick={handleGetConfiguration}
            disabled={loading}
          >
            {loading ? 'Loading...' : 'Get Configuration'}
          </button>
        </div>
        {status && <div className="status-message">{status}</div>}
      </form>
    </div>
  );
};

export default ConfigurationForm;

