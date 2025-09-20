import { Injectable } from '@nestjs/common';
import { Cron, CronExpression } from '@nestjs/schedule';
import { PrismaService } from 'src/prisma/prisma.service';
import { SearchService } from 'src/search/search.service';

@Injectable()
export class SchedulerService {
  constructor(
    private prisma: PrismaService,
    private searchService: SearchService,
  ) {}

  @Cron(CronExpression.EVERY_DAY_AT_5AM)
  async handleCron() {
    const stocks = await this.prisma.stock.findMany();

    for (const stock of stocks) {
      const items = await this.searchService.search(stock.name);

      for (const item of items) {
        const news = await this.prisma.news.create({
          data: {
            date: new Date(),
            summary: item.summary,
            stockId: stock.id,
          },
        });

        if (item.links?.length) {
          const links = item.links.slice(0, 5).map((l) => ({
            title: l.title,
            url: l.url,
            source: l.source ?? null,
            newsId: news.id,
          }));
          await this.prisma.newsLink.createMany({ data: links });
        }

        console.log(news);
      }
    }
  }
}
