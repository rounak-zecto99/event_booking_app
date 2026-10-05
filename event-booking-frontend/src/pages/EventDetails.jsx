import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getEvent, getEventSeats } from "../api/api.js";
import Message from "../components/Message.jsx";
import { formatDate, formatTime, formatPrice } from "../format.js";

export default function EventDetails() {
  const { id } = useParams(); // the :id from the URL
  const [event, setEvent] = useState(null);
  const [seats, setSeats] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getEvent(id), getEventSeats(id)])
      .then(([eventData, seatData]) => { setEvent(eventData); setSeats(seatData); })
      .catch((err) => setError(err.message));
  }, [id]);

  if (error) return <Message type="error">{error}</Message>;
  if (!event) return <Message>Loading event...</Message>;

  const available = seats.filter((s) => s.status === "AVAILABLE").length;
  const booked = seats.length - available;
  const price = event.price ?? seats[0]?.price;

  return (
    <>
      <Link to="/" className="back">Back to events</Link>
      <h1>{event.name}</h1>
      <p className="sub">{event.description}</p>
      <div className="panel details">
        <div><span>Date</span><strong>{formatDate(event.startTime)}</strong></div>
        <div><span>Time</span><strong>{formatTime(event.startTime)} to {formatTime(event.endTime)}</strong></div>
        <div><span>Venue</span><strong>Venue #{event.venueId}</strong></div>
        <div><span>Price</span><strong>{formatPrice(price)}</strong></div>
        <div><span>Available seats</span><strong>{available}</strong></div>
        <div><span>Booked seats</span><strong>{booked}</strong></div>
      </div>
      {seats.length === 0 && <Message>No seats have been set up for this event.</Message>}
      <Link to={`/events/${id}/seats`} className="btn">Select seat</Link>
    </>
  );
}
