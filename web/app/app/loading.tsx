import Container from '../../components/Container';
import NewsGrid from '../../components/NewsGrid';

export default function Loading() {
  const Card = () => (
    <div className="card bg-gray-50 rounded-xl p-4 border border-gray-200 shadow-sm animate-pulse">
      <div className="flex justify-between mb-2">
        <div className="w-44 h-4 bg-gray-200 rounded" />
        <div className="w-20 h-3 bg-gray-200 rounded" />
      </div>
      <div className="grid gap-1.5">
        <div className="w-full h-3 bg-gray-200 rounded" />
        <div className="w-[90%] h-3 bg-gray-200 rounded" />
        <div className="w-[75%] h-3 bg-gray-200 rounded" />
      </div>
    </div>
  );

  return (
    <main>
      <Container>
        <div className="sticky top-0 z-10 py-3 bg-white border-b border-gray-100 mb-3 flex items-center gap-3 flex-wrap">
          <div className="w-60 h-8 bg-gray-100 border border-gray-200 rounded-full" />
          <div className="ml-auto flex gap-2">
            <div className="w-56 h-9 bg-gray-100 border border-gray-200 rounded-md" />
            <div className="w-20 h-9 bg-neutral-900/10 rounded-md" />
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
