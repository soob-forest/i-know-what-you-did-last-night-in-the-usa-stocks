package api.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import api.news.Link;
import api.news.NewsQueryService;
import api.news.NewsResponse;
import api.subscriptions.domain.Stock.StockDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AppUiServiceTest {

  @Mock NewsQueryService newsQueryService;
  AppUiService service;

  @BeforeEach
  void setup() {
    service = new AppUiService(newsQueryService);
  }

  @Test
  void buildsSchemaWithCards() {
    var stock = new StockDto(1L, "Apple Inc.", "AAPL");
    var news = new NewsResponse(stock, "S", "2025-06-01", List.of(new Link("T", "https://x", "x")));
    when(newsQueryService.getNewsFor(any(), any(), any())).thenReturn(List.of(news));

    var out = service.buildSchema("overnight", "", "U_TEST", Optional.empty());
    assertThat(out.get("version")).isEqualTo("v1");
    @SuppressWarnings("unchecked")
    var blocks = (List<UIBlock>) out.get("blocks");
    assertThat(blocks).isNotEmpty();
    assertThat(blocks.get(0).type()).isEqualTo("Toolbar");
    assertThat(blocks.get(1).type()).isEqualTo("Container");
  }

  @Test
  void buildsEmptyStateWhenNoNews() {
    when(newsQueryService.getNewsFor(any(), any(), any())).thenReturn(List.of());
    var out = service.buildSchema("overnight", "", "U_TEST", Optional.empty());
    @SuppressWarnings("unchecked")
    var blocks = (List<UIBlock>) out.get("blocks");
    var container = blocks.get(1);
    assertThat(container.children()).isNotEmpty();
    var child = container.children().get(0);
    assertThat(child.type()).isEqualTo("EmptyState");
  }

  @Test
  void filtersLinksBySchemeAndLimitsCountAndCaches() {
    var stock = new StockDto(1L, "Apple Inc.", "AAPL");
    var links = List.of(
        new Link("ok1", "https://ok/1", "ok"),
        new Link("bad", "javascript:alert(1)", null),
        new Link("ok2", "http://ok/2", null),
        new Link("ok3", "https://ok/3", null),
        new Link("ok4", "https://ok/4", null),
        new Link("ok5", "https://ok/5", null),
        new Link("ok6", "https://ok/6", null)
    );
    var news = new NewsResponse(stock, "S", "2025-06-01", links);
    when(newsQueryService.getNewsFor(any(), any(), any())).thenReturn(List.of(news));

    // adjust config
    service.ttlMillis = 30000;
    service.allowedSchemesProp = "http,https";
    service.maxLinks = 5;

    var out1 = service.buildSchema("overnight", "", "U_TEST", Optional.empty());
    var out2 = service.buildSchema("overnight", "", "U_TEST", Optional.empty());

    verify(newsQueryService, times(1)).getNewsFor(any(), any(), any());

    @SuppressWarnings("unchecked")
    var blocks = (List<UIBlock>) out1.get("blocks");
    var container = blocks.get(1);
    var grid = container.children().get(0);
    var card = grid.children().get(0);
    @SuppressWarnings("unchecked") var props = (java.util.Map<String, Object>) card.props();
    @SuppressWarnings("unchecked") var newsObj = (java.util.Map<String, Object>) props.get("news");
    @SuppressWarnings("unchecked") var keptLinks = (java.util.List<java.util.Map<String, Object>>) newsObj.get("links");
    assertThat(keptLinks.size()).isEqualTo(5);
  }
}
