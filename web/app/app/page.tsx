import Container from '../../components/Container';
import Toolbar from '../../components/Toolbar';
import NewsGrid from '../../components/NewsGrid';
import NewsCard from '../../components/NewsCard';
import type { News } from '../../lib/types';

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

async function getData(range: 'today' | 'overnight' = 'overnight'): Promise<News[]> {
  const res = await fetch(`${API_BASE}/news?range=${range}`, { next: { revalidate: 15 } });
  if (!res.ok) return [];
  return res.json();
}

// components moved to /components: Container, Toolbar, NewsGrid, NewsCard

export default async function Page({ searchParams }: { searchParams: { range?: 'today' | 'overnight', q?: string } }) {
  const range = (searchParams?.range as 'today' | 'overnight') || 'overnight';
  const q = (searchParams?.q || '').trim().toLowerCase();
  const list = await getData(range);
  const items = q
    ? list.filter(n => `${n.stock.name} ${n.stock.ticker}`.toLowerCase().includes(q))
    : list;

  return (
    <main>
      <Container>
      <Toolbar range={range} q={q} />

      {items.length === 0 && (
        <div style={{ opacity: 0.8 }}>표시할 뉴스가 없습니다. 구독 종목 또는 수집 데이터를 확인하세요.</div>
      )}

      <NewsGrid>
        {items.map((n, idx) => (
          <NewsCard key={`${n.stock.ticker}-${idx}`} n={n} />
        ))}
      </NewsGrid>
      </Container>
    </main>
  );
}
