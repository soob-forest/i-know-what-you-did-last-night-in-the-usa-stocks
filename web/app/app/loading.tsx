import Container from '../../components/Container';
import NewsGrid from '../../components/NewsGrid';

export default function Loading() {
  const Card = () => (
    <div className="card" style={{ background: '#f9fafb', borderRadius: 12, padding: 16, border: '1px solid #e5e7eb', boxShadow: '0 1px 2px rgba(0,0,0,0.05)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
        <div style={{ width: 180, height: 16, background: '#e5e7eb', borderRadius: 4 }} />
        <div style={{ width: 80, height: 12, background: '#e5e7eb', borderRadius: 4 }} />
      </div>
      <div style={{ display: 'grid', gap: 6 }}>
        <div style={{ width: '100%', height: 12, background: '#e5e7eb', borderRadius: 4 }} />
        <div style={{ width: '90%', height: 12, background: '#e5e7eb', borderRadius: 4 }} />
        <div style={{ width: '75%', height: 12, background: '#e5e7eb', borderRadius: 4 }} />
      </div>
    </div>
  );

  return (
    <main>
      <Container>
        <div className="toolbar" style={{ display: 'flex', gap: 12, alignItems: 'center', flexWrap: 'wrap' }}>
          <div style={{ width: 240, height: 32, background: '#f3f4f6', border: '1px solid #e5e7eb', borderRadius: 999 }} />
          <div style={{ marginLeft: 'auto', display: 'flex', gap: 8 }}>
            <div style={{ width: 220, height: 36, background: '#f3f4f6', border: '1px solid #e5e7eb', borderRadius: 6 }} />
            <div style={{ width: 80, height: 36, background: '#111111', opacity: .1, borderRadius: 6 }} />
          </div>
        </div>
        <NewsGrid>
          <Card />
          <Card />
          <Card />
        </NewsGrid>
      </Container>
    </main>
  );
}
