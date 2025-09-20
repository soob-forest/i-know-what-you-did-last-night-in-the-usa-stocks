export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body style={{ fontFamily: 'system-ui, sans-serif', margin: 0, padding: 16, background: '#0b1020', color: '#e6e9ef' }}>
        <header style={{ marginBottom: 16 }}>
          <h1 style={{ margin: 0, fontSize: 20 }}>I Know What You Did Last Night (US Stocks)</h1>
        </header>
        {children}
      </body>
    </html>
  );
}

