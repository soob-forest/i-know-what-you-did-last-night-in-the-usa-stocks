import SourceBadge from './SourceBadge';
import type { News } from '../lib/types';

export default function NewsCard({ n }: { n: News }) {
  const bullets = n.summary.split('\n').filter(Boolean).slice(0, 5);
  const favicon = (url: string, source?: string) => {
    let host = source;
    try {
      host = host || new URL(url).hostname;
    } catch {}
    return `https://www.google.com/s2/favicons?sz=16&domain=${encodeURIComponent(host || '')}`;
  };
  return (
    <article className="card" style={{
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
                <a href={l.url} target="_blank" rel="noreferrer" style={{ color: '#1d4ed8', display: 'inline-flex', alignItems: 'center', gap: 6 }}>
                  <img src={favicon(l.url, l.source)} alt="" width={16} height={16} style={{ borderRadius: 3 }} />
                  <span>{l.title}</span>
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
                    <a href={l.url} target="_blank" rel="noreferrer" style={{ color: '#1d4ed8', display: 'inline-flex', alignItems: 'center', gap: 6 }}>
                      <img src={favicon(l.url, l.source)} alt="" width={16} height={16} style={{ borderRadius: 3 }} />
                      <span>{l.source ? `[${l.source}] ` : ''}{l.title}</span>
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

