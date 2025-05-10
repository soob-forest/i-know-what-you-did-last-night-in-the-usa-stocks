package api.subscriptions.domain;

import api.commons.domain.BaseEntity;
import api.members.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  Time pushAt;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member_id")
  private Member member;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscription", cascade = CascadeType.ALL)
  private List<SubscriptionStock> subscriptionStocks;

  @Builder
  public Subscription(Time pushAt, List<SubscriptionStock> subscriptionStocks) {
    this.pushAt = pushAt;
    this.subscriptionStocks = subscriptionStocks;
  }

  public static Subscription createForInit(Time pushAt) {
    return Subscription.builder().pushAt(pushAt).subscriptionStocks(new ArrayList<>()).build();
  }

  public void subscribe(Stock stock) {
    if (subscriptionStocks.stream().map(SubscriptionStock::getStock).toList().contains(stock)) {
      throw new IllegalArgumentException("이미 구독중인 주식입니다.");
    }
    subscriptionStocks.add(SubscriptionStock.builder().stock(stock).subscription(this).build());
  }

  public List<Stock> getSubscribingStocks() {
    return subscriptionStocks.stream().map(SubscriptionStock::getStock).toList();
  }

  public Member getMember() {
    return member;
  }
}
