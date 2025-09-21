export type Link = { title: string; url: string; source?: string };
export type Stock = { name: string; ticker: string };
export type News = { stock: Stock; summary: string; date: string; links: Link[] };

