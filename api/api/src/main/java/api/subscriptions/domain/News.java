package api.subscriptions.domain;

import api.commons.domain.BaseEntity;
import api.subscriptions.domain.Stock.StockDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
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
  @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
  List<NewsLink> links;

  public NewsDto toNewsDto() {
    var linkDtos = links == null ? List.<NewsLink.LinkDto>of() : links.stream().map(NewsLink::toLinkDto).toList();
    return new NewsDto(stock.toStockDto(), summary, linkDtos);
  }

  public static record NewsDto(StockDto stock, String summary, List<NewsLink.LinkDto> links) {

  }

  public LocalDate getDate() {
    return date;
  }
}
