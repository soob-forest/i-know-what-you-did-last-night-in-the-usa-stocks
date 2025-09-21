export default function Container({ children }: { children: React.ReactNode }) {
  return <div className="max-w-[1080px] mx-auto">{children}</div>;
}
