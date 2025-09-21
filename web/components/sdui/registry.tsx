import Toolbar from '../../components/Toolbar';
import Container from '../../components/Container';
import NewsGrid from '../../components/NewsGrid';
import NewsCard from '../../components/NewsCard';
import EmptyState from '../../components/EmptyState';
import type { UIBlock } from '../../lib/sdui/types';
import type { News } from '../../lib/types';
import UnsupportedBlock from '../../components/UnsupportedBlock';
import { JSX } from 'react';

type RegistryEntry = (props?: Record<string, any>, children?: UIBlock[]) => JSX.Element | null;

function isValidNews(obj: any): obj is News {
  return !!obj && !!obj.stock && typeof obj.stock.name === 'string' && typeof obj.stock.ticker === 'string' && typeof obj.summary === 'string' && typeof obj.date === 'string' && Array.isArray(obj.links);
}

export const componentRegistry: Record<string, RegistryEntry> = {
  Toolbar: (props) => (
    <Toolbar range={(props?.range as 'overnight' | 'today') ?? 'overnight'} q={(props?.q as string) ?? ''} />
  ),
  Container: (_props, children) => (
    <Container>{children && children.length ? <Renderer blocks={children} /> : null}</Container>
  ),
  NewsGrid: (_props, children) => <NewsGrid>{children && children.length ? <Renderer blocks={children} /> : null}</NewsGrid>,
  NewsCard: (props) => {
    const n = props?.news;
    if (!isValidNews(n)) return null;
    return <NewsCard n={n} />;
  },
  EmptyState: (props) => <EmptyState message={typeof props?.message === 'string' ? props!.message : '표시할 항목이 없습니다.'} />,
};

export function Renderer({ blocks }: { blocks: UIBlock[] }) {
  return (
    <>
      {blocks.map((b, idx) => {
        const render = componentRegistry[b.type];
        if (!render) return <UnsupportedBlock key={`${b.type}-${idx}`} />;
        return <div key={`${b.type}-${idx}`}>{render(b.props, b.children)}</div>;
      })}
    </>
  );
}
