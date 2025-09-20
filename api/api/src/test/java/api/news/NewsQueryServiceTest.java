package api.news;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import api.members.domain.Member;
import api.members.domain.MemberRepository;
import api.subscriptions.domain.News;
import api.subscriptions.domain.NewsLink;
import api.subscriptions.domain.NewsRepository;
import api.subscriptions.domain.Stock;
import api.subscriptions.domain.Stock.StockDto;
import api.subscriptions.domain.StockRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class NewsQueryServiceTest {

  @Mock MemberRepository memberRepository;
  @Mock StockRepository stockRepository;
  @Mock NewsRepository newsRepository;

  NewsQueryService service;

  @BeforeEach
  void setup() {
    service = new NewsQueryService(memberRepository, stockRepository, newsRepository);
  }

  @Test
  void returnsNewsForTickersWithLinks() {
    var stockMock = mock(Stock.class);
    when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stockMock));

    var newsMock = mock(News.class);
    var link = new NewsLink.LinkDto("Title A", "https://example.com/a", "example.com");
    var stockDto = new StockDto(1L, "Apple Inc.", "AAPL");
    when(newsMock.toNewsDto()).thenReturn(new News.NewsDto(stockDto, "Summary text", List.of(link)));
    when(newsMock.getDate()).thenReturn(LocalDate.now());

    when(newsRepository.findAllWithLinksByDateAndStockIn(any(LocalDate.class), any()))
        .thenReturn(List.of(newsMock));

    var out = service.getNewsFor("overnight", Optional.of(List.of("AAPL")), "U_TEST");

    assertThat(out).hasSize(1);
    assertThat(out.get(0).stock().ticker()).isEqualTo("AAPL");
    assertThat(out.get(0).links()).hasSize(1);
    assertThat(out.get(0).links().get(0).url()).contains("example.com");
  }

  @Test
  void fallsBackToMemberSubscriptionsWhenNoTickersProvided() {
    var stockMock = mock(Stock.class);
    var memberMock = mock(Member.class);
    when(memberMock.getSubscribingStocks()).thenReturn(List.of(stockMock));
    when(memberRepository.findByUserId("U_TEST")).thenReturn(Optional.of(memberMock));

    var newsMock = mock(News.class);
    var stockDto = new StockDto(2L, "Tesla", "TSLA");
    when(newsMock.toNewsDto()).thenReturn(new News.NewsDto(stockDto, "S", List.of()));
    when(newsMock.getDate()).thenReturn(LocalDate.now());
    when(newsRepository.findAllWithLinksByDateAndStockIn(any(LocalDate.class), any()))
        .thenReturn(List.of(newsMock));

    var out = service.getNewsFor("today", Optional.empty(), "U_TEST");
    assertThat(out).hasSize(1);
    assertThat(out.get(0).stock().ticker()).isEqualTo("TSLA");
  }
}

