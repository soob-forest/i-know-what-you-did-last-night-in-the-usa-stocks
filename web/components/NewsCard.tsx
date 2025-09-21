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
    <article className="card bg-gray-50 rounded-xl p-4 border border-gray-200 shadow-sm transition will-change-transform will-change-shadow">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline', marginBottom: 8 }}>
        <h3 className="m-0 text-base text-neutral-900">{n.stock.name} ({n.stock.ticker})</h3>
        <time className="text-xs text-gray-500">{n.date}</time>
      </div>
      {bullets.length > 1 ? (
        <ul className="m-0 mb-2 pl-5 leading-6 list-disc">
          {bullets.map((b, i) => (
            <li key={i}>{b}</li>
          ))}
        </ul>
      ) : (
        <p className="m-0 mb-2 whitespace-pre-wrap leading-7">{n.summary}</p>
      )}
      {n.links?.length ? (
        <div>
          <div className="mb-1.5">
            {n.links.slice(0, 3).map((l, i) => (
              <SourceBadge key={`${l.url}-${i}`} source={l.source} />
            ))}
          </div>
          <ul className="m-0 pl-4 list-disc">
            {n.links.slice(0, 3).map((l, i) => (
              <li key={i}>
                <a href={l.url} target="_blank" rel="noreferrer" className="text-blue-700 inline-flex items-center gap-1.5">
                  <img src={favicon(l.url, l.source)} alt="" width={16} height={16} className="rounded" />
                  <span>{l.title}</span>
                </a>
              </li>
            ))}
          </ul>
          {n.links.length > 3 && (
            <details className="mt-1.5">
              <summary className="cursor-pointer text-gray-800">원문 더보기</summary>
              <ul className="mt-1.5 m-0 pl-4 list-disc">
                {n.links.slice(3).map((l, i) => (
                  <li key={`more-${i}`}>
                    <a href={l.url} target="_blank" rel="noreferrer" className="text-blue-700 inline-flex items-center gap-1.5">
                      <img src={favicon(l.url, l.source)} alt="" width={16} height={16} className="rounded" />
                      <span>{l.source ? `[${l.source}] ` : ''}{l.title}</span>
                    </a>
                  </li>
                ))}
              </ul>
            </details>
          )}
        </div>
      ) : (
        <div className="opacity-70">관련 링크 없음</div>
      )}
    </article>
  );
}
