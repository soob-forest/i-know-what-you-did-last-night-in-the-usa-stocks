package api.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
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
  @MockBean AppUiService appUiService;
  @MockBean org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMappingContext;

  @Test
  void returnsUiBlocksSchema() throws Exception {
    var schema = Map.of(
        "version", "v1",
        "blocks", List.of(
            new UIBlock("Toolbar", Map.of("range", "overnight", "q", "AAPL"), List.of()),
            new UIBlock("Container", Map.of(), List.of(
                new UIBlock("NewsGrid", Map.of(), List.of(
                    new UIBlock("NewsCard", Map.of(
                        "news", Map.of(
                            "stock", Map.of("name", "Apple Inc.", "ticker", "AAPL"),
                            "summary", "...",
                            "date", "2025-06-01",
                            "links", List.of()
                        )
                    ), List.of())
                ))
            ))
        )
    );
    when(appUiService.buildSchema(any(), any(), any(), any())).thenReturn(schema);

    mockMvc.perform(get("/ui/app").param("range", "overnight").param("q", "AAPL"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.blocks[0].type").value("Toolbar"))
        .andExpect(jsonPath("$.blocks[1].type").value("Container"))
        .andExpect(jsonPath("$.blocks[1].children[0].type").value("NewsGrid"))
        .andExpect(jsonPath("$.blocks[1].children[0].children[0].type").value("NewsCard"))
        .andExpect(jsonPath("$.blocks[1].children[0].children[0].props.news.stock.ticker").value("AAPL"));
  }
}
