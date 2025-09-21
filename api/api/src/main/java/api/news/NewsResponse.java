package api.news;

import api.subscriptions.domain.Stock.StockDto;
import java.util.List;

public record NewsResponse(StockDto stock, String summary, String date, List<Link> links) {
}
