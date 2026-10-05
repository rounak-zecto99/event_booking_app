// Small reusable box for loading / error / empty messages.
export default function Message({ type = "info", children, onRetry }) {
  return (
    <div className={`notice ${type}`}>
      <p>{children}</p>
      {onRetry && <button className="btn secondary" onClick={onRetry}>Try again</button>}
    </div>
  );
}
