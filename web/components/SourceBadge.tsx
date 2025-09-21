export default function SourceBadge({ source }: { source?: string }) {
  if (!source) return null;
  return (
    <span style={{
      display: 'inline-block',
      border: '1px solid #e5e7eb',
      background: '#f3f4f6',
      color: '#111111',
      padding: '2px 8px',
      borderRadius: 999,
      fontSize: 12,
      marginRight: 6,
    }}>{source}</span>
  );
}

