import { Injectable } from '@nestjs/common';
import { openai } from '../libs/openai';
import wretch from 'wretch';
import { XMLParser } from 'fast-xml-parser';

@Injectable()
export class SearchService {
  private readonly parser = new XMLParser();
  constructor() {}
  async search(stock: string): Promise<any> {
    const response = wretch(
      `https://news.google.com/rss/search?q=intitle:${stock}+when:8h&hl=en-US&gl=US&ceid=US:en&sort=date`,
    ).get();

    const text = await response.text();

    const news: { id: string; title: string; link: string }[] = this.parser
      .parse(text)
      .rss.channel.item.map(({ guid, title, link }) => {
        return { id: guid, title, link };
      });

    const titles = news
      .slice(0, 20)
      .map(({ title }, index) => `${index}: ${title}`)
      .join('\n');

    const topicSystemPrompt = `You are an AI assistant that guesses the topic of the news.
    You need to look at the title of a given news, guess the topic in a sentence, and return the result in json format.
    '''json
    [{
      id: number,
      title:"title",
      topic:"topic",
    }]
    '''`;

    const topicResult = await openai.chat.completions.create({
      messages: [
        { role: 'system', content: topicSystemPrompt },
        { role: 'user', content: titles },
      ],
      model: 'gpt-3.5-turbo',
      max_tokens: 1500,
      temperature: 0.7,
      top_p: 1,
    });

    console.log(topicResult.choices[0].message.content);

    const summaryResult = await openai.chat.completions.create({
      messages: [
        {
          role: 'system',
          content: `Using the given article, similar topics should be grouped and summarized in the following order.

          1. Group articles with similar topics.
          2. Summarize the contents using the topics of grouped articles.
          3. Returns the results in json format, including grouped ids and summary sentences together in the results.
      
          '''json
          [{
            id:number,
            articleIds:[],
            summary:"",
          }]
          '''`,
        },
        { role: 'user', content: topicResult.choices[0].message.content },
      ],
      model: 'gpt-3.5-turbo',
      max_tokens: 1500,
      temperature: 0.7,
      top_p: 1,
    });

    console.log(summaryResult.choices[0].message.content);

    const summaries: { id: number; summary: string }[] = JSON.parse(
      summaryResult.choices[0].message.content,
    );

    return summaries.map(({ summary }) => summary).slice(0, 10);

    // const scoredTopicResponse = await openai.chat.completions.create({
    //   messages: [
    //     {
    //       role: 'system',
    //       content: `It thinks about whether the summary is likely to affect the stock price, scores it with a rationale for thinking so, and returns the result in JSON format.
    //       The more likely it is to affect the stock price, the higher the score, which ranges from 0 to 100.

    //        '''json
    //                  [{
    //                    articleIds: number[],
    //                    reason:reason,
    //                    score: number(0 - 100)
    //                 }]
    //       '''`,
    //     },
    //     { role: 'user', content: summaryResult.choices[0].message.content },
    //   ],
    //   model: 'gpt-3.5-turbo',
    //   max_tokens: 1500,
    //   temperature: 0.7,
    //   top_p: 1,
    // });

    // const sortedTopics = JSON.parse(
    //   scoredTopicResponse.choices[0].message.content,
    // ).sort((a, b) => {
    //   if (Math.abs(a.score) > Math.abs(b.score)) {
    //     return -1;
    //   }
    //   if (Math.abs(a.score) < Math.abs(b.score)) {
    //     return 1;
    //   }
    //   return 0;
    // });

    // return sortedTopics.choices[0].message.content;
  }
}
