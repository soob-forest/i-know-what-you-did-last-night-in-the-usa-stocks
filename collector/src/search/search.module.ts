import { Module } from '@nestjs/common';
import { SearchService } from './search.service';

@Module({
  imports: [],
  providers: [SearchService],
})
export class SearchModule {}
