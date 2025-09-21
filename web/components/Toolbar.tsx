export default function Toolbar({ range, q }: { range: 'overnight' | 'today'; q: string }) {
  const chip = (active: boolean) =>
    `px-3 py-1.5 rounded-full border ${active ? 'bg-neutral-900 text-white border-neutral-900' : 'text-neutral-900 border-gray-300'}`;
  return (
    <div className="sticky top-0 z-10 py-3 bg-white border-b border-gray-100 mb-3 flex items-center gap-3 flex-wrap">
      <nav className="flex items-center gap-2">
        <a href="/app?range=overnight" className={chip(range === 'overnight')}>Overnight</a>
        <a href="/app?range=today" className={chip(range === 'today')}>Today</a>
      </nav>
      <form method="get" action="/app" className="ml-auto flex gap-2">
        <input type="hidden" name="range" value={range} />
        <input
          name="q"
          placeholder="티커/회사명 검색"
          defaultValue={q}
          className="bg-white text-neutral-900 border border-gray-300 rounded-md px-3 py-2 min-w-[200px]"
        />
        <button type="submit" className="bg-neutral-900 text-white border border-neutral-900 rounded-md px-3 py-2">검색</button>
      </form>
    </div>
  );
}
