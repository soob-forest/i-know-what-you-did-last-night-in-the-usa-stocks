import Toolbar from '../../components/Toolbar';
import Container from '../../components/Container';
import NewsGrid from '../../components/NewsGrid';
import NewsCard from '../../components/NewsCard';
import type { UIBlock } from '../../lib/sdui/types';
import type { News } from '../../lib/types';

type RegistryEntry = (props?: Record<string, any>, children?: UIBlock[]) => JSX.Element | null;

export const componentRegistry: Record<string, RegistryEntry> = {
  Toolbar: (props) => (
    <Toolbar range={(props?.range as 'overnight' | 'today') ?? 'overnight'} q={(props?.q as string) ?? ''} />
  ),
  Container: (_props, children) => (
    <Container>{children && children.length ? <Renderer blocks={children} /> : null}</Container>
  ),
  NewsGrid: (_props, children) => <NewsGrid>{children && children.length ? <Renderer blocks={children} /> : null}</NewsGrid>,
  NewsCard: (props) => {
    const n = props?.news as News | undefined;
    if (!n) return null;
    return <NewsCard n={n} />;
  },
};

export function Renderer({ blocks }: { blocks: UIBlock[] }) {
  return (
    <>
      {blocks.map((b, idx) => {
        const render = componentRegistry[b.type];
        if (!render) return null;
        return <div key={`${b.type}-${idx}`}>{render(b.props, b.children)}</div>;
      })}
    </>
  );
}

