// All backend calls live here, so components never write fetch() themselves.
const API_BASE_URL = "http://localhost:8080";

function makeError(status, message) {
  const error = new Error(message);
  error.status = status; // lets pages check for 409, 404, etc.
  return error;
}

function friendlyMessage(status) {
  if (status === 400) return "The request was not valid. Please try again.";
  if (status === 404) return "We could not find what you were looking for.";
  if (status === 409) return "Sorry, this seat was just booked by someone else.";
  return "Something went wrong on the server. Please try again later.";
}

// One helper: sends the request, converts failures into friendly errors.
async function request(path, options) {
  let response;
  try {
    response = await fetch(API_BASE_URL + path, options);
  } catch {
    // fetch only throws when the server cannot be reached at all
    throw makeError(0, "Cannot reach the server. Check that the backend is running on port 8080.");
  }
  if (!response.ok) throw makeError(response.status, friendlyMessage(response.status));
  const isJson = (response.headers.get("content-type") || "").includes("json");
  return isJson ? response.json() : response.text();
}

// REQUIRES a backend endpoint: GET /events (not in the backend yet)
export function getEvents() {
  return request("/events");
}

// No GET /events/{id} exists, so we reuse GET /events and pick the right one.
export async function getEvent(id) {
  const events = await getEvents();
  const event = events.find((e) => String(e.id) === String(id));
  if (!event) throw makeError(404, friendlyMessage(404));
  return event;
}

// REQUIRES a backend endpoint: GET /events/{id}/seats (not in the backend yet)
export function getEventSeats(eventId) {
  return request(`/events/${eventId}/seats`);
}

// EXISTS: POST /bookings. Success returns the text "Booking successful".
export function createBooking(eventId, eventSeatId) {
  return request("/bookings", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ eventId, eventSeatId }),
  });
}
