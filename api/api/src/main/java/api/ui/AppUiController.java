package api.ui;

import api.news.NewsQueryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppUiController {

  private final NewsQueryService newsQueryService;

  @GetMapping("/ui/app")
  public ResponseEntity<Map<String, Object>> getAppUi(
      @RequestParam(name = "range", defaultValue = "overnight") String range,
      @RequestParam(name = "tickers", required = false) String tickers,
      @RequestParam(name = "q", required = false) String q
  ) {
    String testUserId = Optional.ofNullable(System.getenv("TEST_USER_ID")).orElse("U_TEST");
    Optional<List<String>> tickersOpt = Optional.ofNullable(tickers)
        .filter(s -> !s.isBlank())
        .map(s -> List.of(s.split(",")));

    var news = newsQueryService.getNewsFor(range, tickersOpt, testUserId);
    // Optional client-side filter
    if (q != null && !q.isBlank()) {
      String qq = q.toLowerCase();
      news = news.stream().filter(n -> (n.stock().name() + " " + n.stock().ticker()).toLowerCase().contains(qq)).toList();
    }

    List<UIBlock> blocks = new ArrayList<>();

    // Toolbar
    Map<String, Object> toolbarProps = new HashMap<>();
    toolbarProps.put("range", range);
    toolbarProps.put("q", q == null ? "" : q);
    blocks.add(new UIBlock("Toolbar", toolbarProps, List.of()));

    // Container > NewsGrid > NewsCard[]
    List<UIBlock> gridChildren = new ArrayList<>();
    for (var n : news) {
      Map<String, Object> newsCardProps = new HashMap<>();
      Map<String, Object> stock = Map.of("name", n.stock().name(), "ticker", n.stock().ticker());
      List<Map<String, Object>> links = n.links().stream()
          .map(l -> {
            Map<String, Object> m = new HashMap<>();
            m.put("title", l.title());
            m.put("url", l.url());
            m.put("source", l.source());
            return m;
          })
          .toList();
      Map<String, Object> newsObj = new HashMap<>();
      newsObj.put("stock", stock);
      newsObj.put("summary", n.summary());
      newsObj.put("date", n.date());
      newsObj.put("links", links);
      newsCardProps.put("news", newsObj);
      gridChildren.add(new UIBlock("NewsCard", newsCardProps, List.of()));
    }

    UIBlock newsGrid = new UIBlock("NewsGrid", Map.of(), gridChildren);
    UIBlock container = new UIBlock("Container", Map.of(), List.of(newsGrid));
    blocks.add(container);

    Map<String, Object> body = Map.of("blocks", blocks);
    return ResponseEntity.ok(body);
  }
}
