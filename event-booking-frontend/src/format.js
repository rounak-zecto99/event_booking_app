export function formatDate(iso) {
  return new Date(iso).toLocaleDateString("en-IN", { weekday: "short", day: "numeric", month: "short", year: "numeric" });
}
export function formatTime(iso) {
  return new Date(iso).toLocaleTimeString("en-IN", { hour: "numeric", minute: "2-digit" });
}
export function formatPrice(price) {
  return price == null ? "-" : "₹" + Number(price).toLocaleString("en-IN");
}
