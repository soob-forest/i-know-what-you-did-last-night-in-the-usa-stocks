export default function SourceBadge({ source }: { source?: string }) {
  if (!source) return null;
  return (
    <span className="inline-block border border-gray-200 bg-gray-100 text-neutral-900 px-2 py-0.5 rounded-full text-xs mr-1.5">{source}</span>
  );
}
