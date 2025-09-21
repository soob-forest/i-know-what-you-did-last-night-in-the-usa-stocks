type Link = { title: string; url: string; source?: string };
type Stock = { name: string; ticker: string };
type News = { stock: Stock; summary: string; date: string; links: Link[] };

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

async function getData(range: 'today' | 'overnight' = 'overnight'): Promise<News[]> {
  const res = await fetch(`${API_BASE}/news?range=${range}`, { next: { revalidate: 15 } });
  if (!res.ok) return [];
  return res.json();
}

function SourceBadge({ source }: { source?: string }) {
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

function NewsCard({ n }: { n: News }) {
  const bullets = n.summary.split('\n').filter(Boolean).slice(0, 5);
  return (
    <article style={{
      background: '#f9fafb',
      borderRadius: 12,
      padding: 16,
      border: '1px solid #e5e7eb',
      boxShadow: '0 1px 2px rgba(0,0,0,0.05)'
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline', marginBottom: 8 }}>
        <h3 style={{ margin: 0, fontSize: 16, color: '#111111' }}>
          {n.stock.name} ({n.stock.ticker})
        </h3>
        <time style={{ color: '#6b7280', fontSize: 12 }}>{n.date}</time>
      </div>
      {bullets.length > 1 ? (
        <ul style={{ margin: '0 0 8px 0', paddingLeft: 18, lineHeight: 1.5 }}>
          {bullets.map((b, i) => (
            <li key={i}>{b}</li>
          ))}
        </ul>
      ) : (
        <p style={{ margin: '0 0 8px 0', whiteSpace: 'pre-wrap', lineHeight: 1.6 }}>{n.summary}</p>
      )}
      {n.links?.length ? (
        <div>
          <div style={{ marginBottom: 6 }}>
            {n.links.slice(0, 3).map((l, i) => (
              <SourceBadge key={`${l.url}-${i}`} source={l.source} />
            ))}
          </div>
          <ul style={{ margin: 0, paddingLeft: 16 }}>
            {n.links.slice(0, 3).map((l, i) => (
              <li key={i}>
                <a href={l.url} target="_blank" rel="noreferrer" style={{ color: '#1d4ed8' }}>
                  {l.title}
                </a>
              </li>
            ))}
          </ul>
          {n.links.length > 3 && (
            <details style={{ marginTop: 6 }}>
              <summary style={{ cursor: 'pointer', color: '#1f2937' }}>원문 더보기</summary>
              <ul style={{ margin: '6px 0 0', paddingLeft: 16 }}>
                {n.links.slice(3).map((l, i) => (
                  <li key={`more-${i}`}>
                    <a href={l.url} target="_blank" rel="noreferrer" style={{ color: '#1d4ed8' }}>
                      {l.source ? `[${l.source}] ` : ''}{l.title}
                    </a>
                  </li>
                ))}
              </ul>
            </details>
          )}
        </div>
      ) : (
        <div style={{ opacity: 0.7 }}>관련 링크 없음</div>
      )}
    </article>
  );
}

export default async function Page({ searchParams }: { searchParams: { range?: 'today' | 'overnight', q?: string } }) {
  const range = (searchParams?.range as 'today' | 'overnight') || 'overnight';
  const q = (searchParams?.q || '').trim().toLowerCase();
  const list = await getData(range);
  const items = q
    ? list.filter(n => `${n.stock.name} ${n.stock.ticker}`.toLowerCase().includes(q))
    : list;

  return (
    <main>
      <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 12, flexWrap: 'wrap' }}>
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

      {items.length === 0 && (
        <div style={{ opacity: 0.8 }}>표시할 뉴스가 없습니다. 구독 종목 또는 수집 데이터를 확인하세요.</div>
      )}

      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: 16 }}>
        {items.map((n, idx) => (
          <NewsCard key={`${n.stock.ticker}-${idx}`} n={n} />
        ))}
      </section>
    </main>
  );
}
