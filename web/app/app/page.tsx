type Link = { title: string; url: string; source?: string };
type Stock = { name: string; ticker: string };
type News = { stock: Stock; summary: string; date: string; links: Link[] };

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

async function getData(range: 'today' | 'overnight' = 'overnight'): Promise<News[]> {
  const res = await fetch(`${API_BASE}/news?range=${range}`, { next: { revalidate: 30 } });
  if (!res.ok) return [];
  return res.json();
}

export default async function Page({ searchParams }: { searchParams: { range?: 'today' | 'overnight' } }) {
  const range = searchParams?.range || 'overnight';
  const items = await getData(range);

  return (
    <main>
      <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 12 }}>
        <span>Range:</span>
        <a href="/app?range=overnight" style={{ color: range === 'overnight' ? '#fff' : '#7f8fa6' }}>Overnight</a>
        <a href="/app?range=today" style={{ color: range === 'today' ? '#fff' : '#7f8fa6' }}>Today</a>
      </div>
      {items.length === 0 && (
        <div style={{ opacity: 0.8 }}>표시할 뉴스가 없습니다. 구독 종목 또는 수집 데이터를 확인하세요.</div>
      )}
      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 16 }}>
        {items.map((n, idx) => (
          <article key={`${n.stock.ticker}-${idx}`} style={{ background: '#131a33', borderRadius: 8, padding: 16, border: '1px solid #1f2a4d' }}>
            <h3 style={{ margin: '0 0 8px 0' }}>{n.stock.name} ({n.stock.ticker})</h3>
            <p style={{ margin: '0 0 8px 0', whiteSpace: 'pre-wrap', lineHeight: 1.4 }}>{n.summary}</p>
            {n.links?.length ? (
              <ul style={{ margin: 0, paddingLeft: 16 }}>
                {n.links.slice(0, 3).map((l, i) => (
                  <li key={i}>
                    <a href={l.url} target="_blank" rel="noreferrer" style={{ color: '#4fa3ff' }}>
                      {l.source ? `[${l.source}] ` : ''}{l.title}
                    </a>
                  </li>
                ))}
              </ul>
            ) : (
              <div style={{ opacity: 0.7 }}>관련 링크 없음</div>
            )}
          </article>
        ))}
      </section>
    </main>
  );
}

