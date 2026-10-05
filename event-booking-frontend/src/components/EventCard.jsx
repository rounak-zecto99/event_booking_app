import { Link } from "react-router-dom";
import { formatDate, formatTime, formatPrice } from "../format.js";

// Props: "event" is passed down from the Events page.
export default function EventCard({ event }) {
  return (
    <article className="card">
      <div className="card-top">
        <span className={`badge ${event.status === "ACTIVE" ? "ok" : "off"}`}>{event.status}</span>
        <span className="price">{formatPrice(event.price)}</span>
      </div>
      <h2>{event.name}</h2>
      <p className="when">{formatDate(event.startTime)}</p>
      <p className="when">{formatTime(event.startTime)} to {formatTime(event.endTime)}</p>
      <p className="desc">{event.description}</p>
      <Link to={`/events/${event.id}`} className="btn">View event</Link>
    </article>
  );
}
