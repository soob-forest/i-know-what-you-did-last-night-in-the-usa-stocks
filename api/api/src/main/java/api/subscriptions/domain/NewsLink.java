package api.subscriptions.domain;

import api.commons.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsLink extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  String title;
  String url;
  String source;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "news_id")
  News news;

  public LinkDto toLinkDto() {
    return new LinkDto(title, url, source);
  }

  public static record LinkDto(String title, String url, String source) {}
}

