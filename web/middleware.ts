import { NextRequest, NextResponse } from 'next/server';

export function middleware(req: NextRequest) {
  const { pathname, searchParams } = new URL(req.url);
  if (pathname === '/__test__/news') {
    const range = searchParams.get('range') || 'overnight';
    if (range === 'error') {
      return new NextResponse('error', { status: 500 });
    }
    const body = (() => {
      if (range === 'overnight') {
        return [
          {
            stock: { name: 'Apple Inc.', ticker: 'AAPL' },
            summary: 'Apple released new products overnight.',
            date: '2025-06-01',
            links: [
              { title: 'Launch article', url: 'https://example.com/a', source: 'example.com' },
              { title: 'Analyst view', url: 'https://news.example.com/b', source: 'news.example.com' },
            ],
          },
          {
            stock: { name: 'Tesla', ticker: 'TSLA' },
            summary: 'Production update and delivery numbers rumored.',
            date: '2025-06-01',
            links: [],
          },
        ];
      }
      if (range === 'today') {
        return [
          {
            stock: { name: 'NVIDIA', ticker: 'NVDA' },
            summary: 'AI conference highlights and roadmap.',
            date: '2025-06-01',
            links: [
              { title: 'Keynote', url: 'https://nvda.example.com/keynote', source: 'nvda.example.com' },
            ],
          },
        ];
      }
      if (range === 'empty') {
        return [];
      }
      return [];
    })();
    return NextResponse.json(body, { status: 200 });
  }
  if (pathname === '/__test__/ui/app') {
    const range = searchParams.get('range') || 'overnight';
    const q = (searchParams.get('q') || '').toLowerCase();
    // Reuse the same stub news
    const makeNews = () => {
      const overnight = [
        {
          stock: { name: 'Apple Inc.', ticker: 'AAPL' },
          summary: 'Apple released new products overnight.',
          date: '2025-06-01',
          links: [
            { title: 'Launch article', url: 'https://example.com/a', source: 'example.com' },
            { title: 'Analyst view', url: 'https://news.example.com/b', source: 'news.example.com' },
          ],
        },
        {
          stock: { name: 'Tesla', ticker: 'TSLA' },
          summary: 'Production update and delivery numbers rumored.',
          date: '2025-06-01',
          links: [],
        },
      ];
      const today = [
        {
          stock: { name: 'NVIDIA', ticker: 'NVDA' },
          summary: 'AI conference highlights and roadmap.',
          date: '2025-06-01',
          links: [
            { title: 'Keynote', url: 'https://nvda.example.com/keynote', source: 'nvda.example.com' },
          ],
        },
      ];
      const base = range === 'today' ? today : overnight;
      return q ? base.filter(n => `${n.stock.name} ${n.stock.ticker}`.toLowerCase().includes(q)) : base;
    };
    const list = makeNews();
    const blocks = [
      { type: 'Toolbar', props: { range, q: searchParams.get('q') || '' } },
      {
        type: 'Container',
        children: [
          {
            type: 'NewsGrid',
            children: list.map(n => ({ type: 'NewsCard', props: { news: n } })),
          },
        ],
      },
    ];
    return NextResponse.json({ blocks });
  }
  return NextResponse.next();
}

export const config = {
  matcher: ['/__test__/:path*'],
};
