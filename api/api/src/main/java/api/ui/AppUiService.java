package api.ui;

import api.news.NewsQueryService;
import api.news.NewsResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUiService {

  private final NewsQueryService newsQueryService;

  @org.springframework.beans.factory.annotation.Value("${ui.cache.ttlMillis:30000}")
  long ttlMillis = 30000;
  @org.springframework.beans.factory.annotation.Value("${ui.url.allowedSchemes:http,https}")
  String allowedSchemesProp = "http,https";
  @org.springframework.beans.factory.annotation.Value("${ui.maxLinks:5}")
  int maxLinks = 5;

  private final java.util.concurrent.ConcurrentHashMap<String, CacheEntry> cache = new java.util.concurrent.ConcurrentHashMap<>();

  public Map<String, Object> buildSchema(String range, String q, String userId, Optional<List<String>> tickersOpt) {
    String cacheKey = keyOf(range, q, userId, tickersOpt);
    long now = System.currentTimeMillis();
    var hit = cache.get(cacheKey);
    if (hit != null && hit.expiresAt >= now) {
      return hit.body;
    }
    var list = newsQueryService.getNewsFor(range, tickersOpt, userId);
    if (q != null && !q.isBlank()) {
      final String qq = q.toLowerCase();
      list = list.stream().filter(n -> (n.stock().name() + " " + n.stock().ticker()).toLowerCase().contains(qq)).toList();
    }

    List<UIBlock> blocks = new ArrayList<>();
    blocks.add(toolbar(range, q));

    List<UIBlock> gridChildren = new ArrayList<>();
    for (var n : list) {
      validatedNewsCard(n).ifPresent(gridChildren::add);
    }

    UIBlock content;
    if (gridChildren.isEmpty()) {
      content = container(emptyState("표시할 뉴스가 없습니다."));
    } else {
      content = container(new UIBlock("NewsGrid", Map.of(), gridChildren));
    }
    blocks.add(content);

    Map<String, Object> body = new HashMap<>();
    body.put("version", "v1");
    body.put("blocks", blocks);
    cache.put(cacheKey, new CacheEntry(body, now + ttlMillis));
    return body;
  }

  private UIBlock toolbar(String range, String q) {
    String r = ("today".equals(range) ? "today" : "overnight");
    Map<String, Object> props = new HashMap<>();
    props.put("range", r);
    props.put("q", q == null ? "" : q);
    return new UIBlock("Toolbar", props, List.of());
  }

  private UIBlock container(UIBlock child) {
    return new UIBlock("Container", Map.of(), List.of(child));
  }

  private UIBlock emptyState(String message) {
    return new UIBlock("EmptyState", Map.of("message", message), List.of());
  }

  private Optional<UIBlock> validatedNewsCard(NewsResponse n) {
    if (n == null || n.stock() == null || n.stock().name() == null || n.stock().ticker() == null || n.summary() == null || n.date() == null) {
      return Optional.empty();
    }
    // Sanitize links (scheme allowlist + required fields)
    var schemes = java.util.Arrays.stream(allowedSchemesProp.split(",")).map(String::trim).filter(s -> !s.isBlank()).collect(java.util.stream.Collectors.toSet());
    var safeLinksStream = n.links() == null ? java.util.stream.Stream.<Map<String, Object>>empty() : n.links().stream().map(l -> {
      if (l == null || l.title() == null || l.url() == null) return null;
      try {
        var uri = new java.net.URI(l.url());
        var scheme = uri.getScheme();
        if (scheme == null || !schemes.contains(scheme.toLowerCase())) return null;
      } catch (Exception e) {
        return null;
      }
      Map<String, Object> m = new HashMap<>();
      m.put("title", l.title());
      m.put("url", l.url());
      if (l.source() != null) m.put("source", l.source());
      return m;
    }).filter(m -> m != null);
    var safeLinks = safeLinksStream.limit(Math.max(0, maxLinks)).toList();

    Map<String, Object> stock = Map.of("name", n.stock().name(), "ticker", n.stock().ticker());
    Map<String, Object> newsObj = new HashMap<>();
    newsObj.put("stock", stock);
    newsObj.put("summary", n.summary());
    newsObj.put("date", n.date());
    newsObj.put("links", safeLinks);
    Map<String, Object> props = Map.of("news", newsObj);
    return Optional.of(new UIBlock("NewsCard", props, List.of()));
  }

  private record CacheEntry(Map<String, Object> body, long expiresAt) {}

  private String keyOf(String range, String q, String userId, Optional<List<String>> tickersOpt) {
    var tickers = tickersOpt.map(list -> String.join("|", list)).orElse("");
    return String.join("#", range == null ? "" : range, q == null ? "" : q, userId == null ? "" : userId, tickers);
  }

  public void invalidateForUser(String userId) {
    if (userId == null) return;
    var it = cache.keySet().iterator();
    while (it.hasNext()) {
      var key = it.next();
      // key = range#q#userId#tickers
      var parts = key.split("#", -1);
      if (parts.length >= 3 && userId.equals(parts[2])) {
        it.remove();
      }
    }
  }
}
