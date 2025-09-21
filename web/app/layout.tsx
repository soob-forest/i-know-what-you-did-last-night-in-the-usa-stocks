export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body style={{ fontFamily: 'system-ui, -apple-system, Segoe UI, Roboto, sans-serif', margin: 0, padding: 16, background: '#ffffff', color: '#111111' }}>
        <header style={{ marginBottom: 16 }}>
          <h1 style={{ margin: 0, fontSize: 20, color: '#111111' }}>I Know What You Did Last Night (US Stocks)</h1>
        </header>
        {children}
      </body>
    </html>
  );
}
