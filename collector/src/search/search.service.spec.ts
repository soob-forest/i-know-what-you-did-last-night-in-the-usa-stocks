import { SearchService } from './search.service';

jest.mock('../libs/openai', () => ({
  openai: {
    chat: {
      completions: {
        create: jest.fn()
      }
    }
  }
}));

// Mock wretch default export to return a chain with .get().text()
jest.mock('wretch', () => {
  const fn = () => ({
    get: () => ({
      text: async () => `<?xml version="1.0"?><rss><channel>
        <item><title>AAPL launches product</title><link>https://example.com/a</link></item>
        <item><title>Analyst upgrades AAPL</title><link>https://news.example.com/b</link></item>
      </channel></rss>`
    })
  });
  // default export
  return {
    __esModule: true,
    default: fn
  };
});

const { openai } = jest.requireMock('../libs/openai');

describe('SearchService', () => {
  it('returns summaries with mapped links from indices', async () => {
    // First OpenAI call (topics)
    (openai.chat.completions.create as jest.Mock).mockResolvedValueOnce({
      choices: [{ message: { content: JSON.stringify([{ id: 0, title: 'AAPL launches product', topic: 'launch' }, { id: 1, title: 'Analyst upgrades AAPL', topic: 'rating' }]) } }]
    });
    // Second OpenAI call (grouped summaries)
    (openai.chat.completions.create as jest.Mock).mockResolvedValueOnce({
      choices: [{ message: { content: JSON.stringify([{ id: 1, articleIds: [0,1], summary: 'Apple launched and got upgraded' }]) } }]
    });

    const svc = new SearchService();
    const out = await svc.search('AAPL');

    expect(out.length).toBeGreaterThan(0);
    expect(out[0].summary).toContain('Apple');
    expect(out[0].links.length).toBe(2);
    expect(out[0].links[0].url).toContain('example.com');
  });
});

