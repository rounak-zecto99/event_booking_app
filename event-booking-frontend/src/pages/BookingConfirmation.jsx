import { Link, useLocation } from "react-router-dom";
import { formatPrice } from "../format.js";

// The backend only returns the text "Booking successful" (no booking id),
// so this page shows the details we passed along from the seat page.
export default function BookingConfirmation() {
  const { state } = useLocation();

  if (!state) {
    return (
      <div className="notice">
        <p>No booking to show. Start by choosing an event.</p>
        <Link to="/" className="btn">Browse events</Link>
      </div>
    );
  }

  return (
    <div className="panel confirm">
      <div className="tick">✓</div>
      <h1>Booking successful</h1>
      <p><span>Event</span> <strong>{state.eventName}</strong></p>
      <p><span>Seat</span> <strong>{state.seat}</strong></p>
      <p><span>Price</span> <strong>{formatPrice(state.price)}</strong></p>
      <Link to="/" className="btn">Back to events</Link>
    </div>
  );
}
