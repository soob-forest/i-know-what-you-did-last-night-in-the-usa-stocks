package api.news;

import api.members.domain.MemberRepository;
import api.subscriptions.domain.NewsRepository;
import api.subscriptions.domain.Stock;
import api.subscriptions.domain.StockRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsQueryService {

  private final MemberRepository memberRepository;
  private final StockRepository stockRepository;
  private final NewsRepository newsRepository;

  public List<NewsResponse> getNewsFor(String range, Optional<List<String>> tickersOpt, String testUserId) {
    List<Stock> stocks = tickersOpt.filter(list -> !list.isEmpty())
        .map(list -> list.stream().map(t -> stockRepository.findByTicker(t).orElse(null)).filter(s -> s != null).toList())
        .orElseGet(() -> memberRepository.findByUserId(testUserId)
            .map(m -> m.getSubscribingStocks())
            .orElseGet(ArrayList::new));

    if (stocks.isEmpty()) {
      return List.of();
    }

    // 현재 엔티티에 시간 정보가 없어 range는 동일하게 당일로 취급
    LocalDate date = LocalDate.now();
    var newsList = newsRepository.findAllWithLinksByDateAndStockIn(date, stocks);
    return newsList.stream().map(n -> {
      var dto = n.toNewsDto();
      return new NewsResponse(
          dto.stock(),
          dto.summary(),
          n.getDate().toString(),
          dto.links().stream().map(l -> new Link(l.title(), l.url(), l.source())).toList()
      );
    }).toList();
  }
}
