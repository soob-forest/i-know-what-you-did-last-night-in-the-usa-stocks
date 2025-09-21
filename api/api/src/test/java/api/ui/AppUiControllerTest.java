package api.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import api.news.Link;
import api.news.NewsQueryService;
import api.news.NewsResponse;
import api.subscriptions.domain.Stock.StockDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AppUiController.class)
@Import(api.config.CorsConfig.class)
class AppUiControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean NewsQueryService newsQueryService;
  @MockBean org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMappingContext;

  @Test
  void returnsUiBlocksSchema() throws Exception {
    var stock = new StockDto(1L, "Apple Inc.", "AAPL");
    var link = new Link("Launch article", "https://example.com/a", "example.com");
    var news = new NewsResponse(stock, "Apple released new products overnight.", "2025-06-01", List.of(link));
    when(newsQueryService.getNewsFor(any(), any(), any())).thenReturn(List.of(news));

    mockMvc.perform(get("/ui/app").param("range", "overnight").param("q", "AAPL"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.blocks[0].type").value("Toolbar"))
        .andExpect(jsonPath("$.blocks[1].type").value("Container"))
        .andExpect(jsonPath("$.blocks[1].children[0].type").value("NewsGrid"))
        .andExpect(jsonPath("$.blocks[1].children[0].children[0].type").value("NewsCard"))
        .andExpect(jsonPath("$.blocks[1].children[0].children[0].props.news.stock.ticker").value("AAPL"));
  }
}

