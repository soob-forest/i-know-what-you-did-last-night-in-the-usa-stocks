import { SearchService } from './search.service';

describe('SearchService', () => {
  let searchService: SearchService;

  beforeEach(async () => {
    searchService = new SearchService();
  });

  describe('open ai 설정 테스트', () => {
    it('테스트', async () => {
      const response = await searchService.search('stock');
      console.log(response);
    }, 30000);
  });
});
