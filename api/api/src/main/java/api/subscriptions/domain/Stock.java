package api.subscriptions.domain;

import api.commons.domain.BaseEntity;
import api.subscriptions.controller.response.SubscribingStock;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String ticker;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
  private List<SubscriptionStock> subscriptionStocks;

  public SubscribingStock toSubscribingStock() {
    return new SubscribingStock(name, ticker);
  }

  public StockDto toStockDto() {
    return new StockDto(id, name, ticker);
  }

  public static record StockDto(Long id, String name, String ticker) {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Stock stock)) {
      return false;
    }

    return id.equals(stock.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
