import { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { getEvent, getEventSeats, createBooking } from "../api/api.js";
import Message from "../components/Message.jsx";
import { formatPrice } from "../format.js";

// Backend may send a label (e.g. "S1"). If not, fall back to the seat id.
const seatLabel = (seat) => seat.seatLabel ?? `Seat ${seat.seatId}`;

export default function SeatSelection() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [seats, setSeats] = useState([]);
  const [selected, setSelected] = useState(null); // the chosen event seat object
  const [loading, setLoading] = useState(true);
  const [booking, setBooking] = useState(false);
  const [error, setError] = useState("");
  const [bookingError, setBookingError] = useState("");

  useEffect(() => {
    Promise.all([getEvent(id), getEventSeats(id)])
      .then(([eventData, seatData]) => { setEvent(eventData); setSeats(seatData); })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [id]);

  async function handleBook() {
    setBooking(true);
    setBookingError("");
    try {
      // IMPORTANT: we send the event seat id (event_seats.id), not seat_id.
      await createBooking(Number(id), selected.id);
      navigate("/booking-success", {
        state: { eventName: event.name, seat: seatLabel(selected), price: selected.price ?? event.price },
      });
    } catch (err) {
      setBookingError(err.message);
      if (err.status === 409) {
        // Seat was taken: reload the seats so the screen shows the truth.
        setSelected(null);
        getEventSeats(id).then(setSeats).catch(() => {});
      }
    } finally {
      setBooking(false);
    }
  }

  if (loading) return <Message>Loading seats...</Message>;
  if (error) return <Message type="error">{error}</Message>;

  const sorted = [...seats].sort((a, b) => a.id - b.id);

  return (
    <>
      <Link to={`/events/${id}`} className="back">Back to event</Link>
      <h1>Choose your seat</h1>
      <p className="sub">{event.name}</p>

      <div className="legend">
        <span><i className="dot free" /> Available</span>
        <span><i className="dot taken" /> Booked</span>
        <span><i className="dot pick" /> Selected</span>
      </div>

      {sorted.length === 0 && <Message>No seats found for this event.</Message>}
      <div className="stage">Stage</div>
      <div className="seats">
        {sorted.map((seat) => {
          const isBooked = seat.status !== "AVAILABLE";
          const isSelected = selected?.id === seat.id;
          return (
            <button
              key={seat.id}
              disabled={isBooked}
              className={`seat ${isBooked ? "taken" : isSelected ? "pick" : "free"}`}
              onClick={() => setSelected(seat)}
            >
              {seatLabel(seat)}
            </button>
          );
        })}
      </div>

      {bookingError && <Message type="error">{bookingError}</Message>}

      {selected && (
        <div className="panel summary">
          <p>Selected seat: <strong>{seatLabel(selected)}</strong></p>
          <p>Price: <strong>{formatPrice(selected.price ?? event.price)}</strong></p>
          <button className="btn" onClick={handleBook} disabled={booking}>
            {booking ? "Booking..." : "Book seat"}
          </button>
        </div>
      )}
    </>
  );
}
