# Event Booking Frontend (React + Vite)

    npm install
    npm run dev      # http://localhost:5173

Backend must run on http://localhost:8080 and needs:
1. GET /events
2. GET /events/{id}/seats  (JSON list: id, eventId, seatId, seatLabel (optional), status, price)
3. CORS allowing http://localhost:5173
