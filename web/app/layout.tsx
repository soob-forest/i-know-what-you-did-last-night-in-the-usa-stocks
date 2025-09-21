export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body style={{ fontFamily: 'system-ui, -apple-system, Segoe UI, Roboto, sans-serif', margin: 0, padding: 16, background: '#ffffff', color: '#111111' }}>
        <header style={{ marginBottom: 16 }}>
          <h1 style={{ margin: 0, fontSize: 20, color: '#111111' }}>I Know What You Did Last Night (US Stocks)</h1>
        </header>
        <style>{`
          .container { max-width: 1080px; margin: 0 auto; }
          .news-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(340px, 1fr)); gap: 16px; }
          .card { transition: box-shadow .15s ease, transform .15s ease; will-change: box-shadow, transform; }
          .card:hover { box-shadow: 0 8px 24px rgba(0,0,0,0.12); transform: translateY(-2px); }
          @media (max-width: 640px) {
            body { padding: 12px; }
            .news-grid { grid-template-columns: 1fr; gap: 12px; }
          }
        `}</style>
        {children}
      </body>
    </html>
  );
}
