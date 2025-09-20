package api.news;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsController {

  private final NewsQueryService newsQueryService;

  public NewsController(NewsQueryService newsQueryService) {
    this.newsQueryService = newsQueryService;
  }

  @GetMapping("/news")
  public ResponseEntity<List<NewsResponse>> getNews(
      @RequestParam(name = "range", defaultValue = "overnight") String range,
      @RequestParam(name = "tickers", required = false) String tickers
  ) {
    String testUserId = Optional.ofNullable(System.getenv("TEST_USER_ID")).orElse("U_TEST");
    Optional<List<String>> tickersOpt = Optional.ofNullable(tickers)
        .filter(s -> !s.isBlank())
        .map(s -> Arrays.stream(s.split(",")).map(String::trim).filter(t -> !t.isEmpty()).toList());

    List<NewsResponse> payload = newsQueryService.getNewsFor(range, tickersOpt, testUserId);
    return ResponseEntity.ok(payload);
  }
}

