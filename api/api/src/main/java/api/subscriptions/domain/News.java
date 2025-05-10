package api.subscriptions.domain;

import api.commons.domain.BaseEntity;
import api.subscriptions.domain.Stock.StockDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  LocalDate date;
  String summary;
  @ManyToOne
  @JoinColumn(name = "stock_id")
  Stock stock;

  public NewsDto toNewsDto() {
    return new NewsDto(stock.toStockDto(), summary);
  }

  public static record NewsDto(StockDto stock, String summary) {

  }
}
