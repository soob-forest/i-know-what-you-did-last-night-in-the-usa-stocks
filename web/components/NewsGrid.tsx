export default function NewsGrid({ children }: { children: React.ReactNode }) {
  return <section className="grid [grid-template-columns:repeat(auto-fill,minmax(340px,1fr))] gap-4 sm:gap-3">{children}</section>;
}
