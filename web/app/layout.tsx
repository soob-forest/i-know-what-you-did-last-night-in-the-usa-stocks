import './globals.css';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body className="min-h-screen bg-white text-neutral-900 antialiased p-4">
        <header className="mb-4">
          <h1 className="m-0 text-xl font-semibold">I Know What You Did Last Night (US Stocks)</h1>
        </header>
        {children}
      </body>
    </html>
  );
}
