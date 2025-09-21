export default function Toolbar({ range, q }: { range: 'overnight' | 'today'; q: string }) {
  return (
    <div className="toolbar" style={{ display: 'flex', gap: 12, alignItems: 'center', flexWrap: 'wrap' }}>
      <nav style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
        <a href="/app?range=overnight" style={{
          color: range === 'overnight' ? '#ffffff' : '#111111',
          background: range === 'overnight' ? '#111111' : 'transparent',
          border: `1px solid ${range === 'overnight' ? '#111111' : '#d1d5db'}`,
          padding: '6px 10px', borderRadius: 999,
          textDecoration: 'none'
        }}>Overnight</a>
        <a href="/app?range=today" style={{
          color: range === 'today' ? '#ffffff' : '#111111',
          background: range === 'today' ? '#111111' : 'transparent',
          border: `1px solid ${range === 'today' ? '#111111' : '#d1d5db'}`,
          padding: '6px 10px', borderRadius: 999,
          textDecoration: 'none'
        }}>Today</a>
      </nav>
      <form method="get" action="/app" style={{ marginLeft: 'auto', display: 'flex', gap: 8 }}>
        <input type="hidden" name="range" value={range} />
        <input
          name="q"
          placeholder="티커/회사명 검색"
          defaultValue={q}
          style={{
            background: '#ffffff', color: '#111111', border: '1px solid #d1d5db', borderRadius: 6,
            padding: '8px 10px', minWidth: 200
          }}
        />
        <button type="submit" style={{
          background: '#111111', color: '#ffffff', border: '1px solid #111111', borderRadius: 6,
          padding: '8px 12px'
        }}>검색</button>
      </form>
    </div>
  );
}

