import { Module } from '@nestjs/common';
import { SchedulerService } from './scheduler.service';
import { SearchModule } from 'src/search/search.module';
import { PrismaService } from 'src/prisma/prisma.service';
import { SearchService } from 'src/search/search.service';

@Module({
  imports: [SearchModule],
  providers: [SchedulerService, PrismaService, SearchService],
})
export class SchedulerModule {}
