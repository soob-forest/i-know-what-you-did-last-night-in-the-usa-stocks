package api.news;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import api.subscriptions.domain.Stock.StockDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = NewsController.class)
class NewsControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean NewsQueryService newsQueryService;

  @Test
  void getNews_returnsOkPayload() throws Exception {
    var payload = List.of(new NewsResponse(new StockDto(1L, "Apple Inc.", "AAPL"), "S", "2025-01-01", List.of(new Link("T", "https://x", "x"))));
    when(newsQueryService.getNewsFor(any(), any(), any())).thenReturn(payload);

    mockMvc.perform(get("/news").param("range", "overnight").param("tickers", "AAPL"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].stock.ticker").value("AAPL"))
        .andExpect(jsonPath("$[0].links[0].title").value("T"));
  }
}

