import OpenAI from 'openai';

export const openai: OpenAI = new OpenAI({
  apiKey: process.env['OPENAI_API_KEY'] ?? '',
});
