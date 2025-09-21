package api.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}

