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

  private final AppUiService appUiService;

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

    var body = appUiService.buildSchema(range, q, testUserId, tickersOpt);
    return ResponseEntity.ok(body);
  }
}
