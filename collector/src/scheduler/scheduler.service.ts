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
      const summaries: string[] = await this.searchService.search(stock.name);

      const news = await this.prisma.news.create({
        data: {
          date: new Date(),
          summary: summaries.join('\n'),
          stockId: stock.id,
        },
      });
      console.log(news);
    }
  }
}
