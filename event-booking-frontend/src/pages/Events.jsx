import { useEffect, useState } from "react";
import { getEvents } from "../api/api.js";
import EventCard from "../components/EventCard.jsx";
import Message from "../components/Message.jsx";

export default function Events() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  function loadEvents() {
    setLoading(true);
    setError("");
    getEvents()
      .then(setEvents)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(loadEvents, []); // runs once when the page opens

  return (
    <>
      <h1>Upcoming events</h1>
      <p className="sub">Pick an event, choose your seat, and book it.</p>
      {loading && <Message>Loading events...</Message>}
      {error && <Message type="error" onRetry={loadEvents}>{error}</Message>}
      {!loading && !error && events.length === 0 && <Message>No events yet. Check back soon.</Message>}
      <div className="grid">
        {events.map((event) => <EventCard key={event.id} event={event} />)}
      </div>
    </>
  );
}
