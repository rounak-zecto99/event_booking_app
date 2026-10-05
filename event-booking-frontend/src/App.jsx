import { Routes, Route, Link } from "react-router-dom";
import Events from "./pages/Events.jsx";
import EventDetails from "./pages/EventDetails.jsx";
import SeatSelection from "./pages/SeatSelection.jsx";
import BookingConfirmation from "./pages/BookingConfirmation.jsx";

// Each <Route> maps a URL to a page component.
export default function App() {
  return (
    <>
      <header className="navbar">
        <Link to="/" className="logo">Eventful</Link>
      </header>
      <main className="container">
        <Routes>
          <Route path="/" element={<Events />} />
          <Route path="/events/:id" element={<EventDetails />} />
          <Route path="/events/:id/seats" element={<SeatSelection />} />
          <Route path="/booking-success" element={<BookingConfirmation />} />
          <Route path="*" element={<p className="notice">Page not found.</p>} />
        </Routes>
      </main>
    </>
  );
}
