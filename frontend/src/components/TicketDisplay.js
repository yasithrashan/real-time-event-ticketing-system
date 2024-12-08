import React from 'react';
import './TicketDisplay.css';

const TicketDisplay = ({ availableTickets }) => {
  return (
    <div className="ticket-display-container">
      <h2>Ticket Availability</h2>
      <p>Current Tickets Available: <strong>{availableTickets}</strong></p>
    </div>
  );
};

export default TicketDisplay;
