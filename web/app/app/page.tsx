import Container from '../../components/Container';
import Toolbar from '../../components/Toolbar';
import NewsGrid from '../../components/NewsGrid';
import NewsCard from '../../components/NewsCard';
import { Renderer } from '../../components/sdui/registry';
import type { UIBlock, UISchemaResponse } from '../../lib/sdui/types';
import type { News } from '../../lib/types';

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

async function getData(range: 'today' | 'overnight' = 'overnight'): Promise<News[]> {
  const res = await fetch(`${API_BASE}/news?range=${range}`, { next: { revalidate: 15 } });
  if (!res.ok) return [];
  return res.json();
}

async function getSchema(range: 'today' | 'overnight', q: string): Promise<UIBlock[]> {
  const qs = new URLSearchParams({ range, q }).toString();
  const res = await fetch(`${API_BASE}/ui/app?${qs}`, { next: { revalidate: 15 } });
  if (!res.ok) return [];
  const json = (await res.json()) as UISchemaResponse;
  return json.blocks || [];
}

// components moved to /components: Container, Toolbar, NewsGrid, NewsCard

export default async function Page({ searchParams }: { searchParams: { range?: 'today' | 'overnight', q?: string, sdui?: string } }) {
  const range = (searchParams?.range as 'today' | 'overnight') || 'overnight';
  const qRaw = (searchParams?.q || '').trim();
  const q = qRaw.toLowerCase();
  const useSchema = searchParams?.sdui === '1';
  const items = useSchema ? [] : (() => { /* placeholder for type */ return [] as News[] })();
  let blocks: UIBlock[] = [];
  if (useSchema) {
    blocks = await getSchema(range, qRaw);
  } else {
    const list = await getData(range);
    blocks = [
      { type: 'Toolbar', props: { range, q: qRaw } },
      {
        type: 'Container',
        children: [
          {
            type: 'NewsGrid',
            children: list
              .filter(n => (q ? `${n.stock.name} ${n.stock.ticker}`.toLowerCase().includes(q) : true))
              .map((n) => ({ type: 'NewsCard', props: { news: n } })),
          },
        ],
      },
    ];
  }

  return (
    <main>
      <Renderer blocks={blocks} />
    </main>
  );
}
