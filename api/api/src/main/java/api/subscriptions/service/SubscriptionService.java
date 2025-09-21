package api.subscriptions.service;

import api.members.domain.MemberRepository;
import api.subscriptions.controller.response.SubscribingStock;
import api.subscriptions.domain.Stock;
import api.subscriptions.domain.StockRepository;
import api.subscriptions.domain.SubscriptionRepository;
import java.sql.Time;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import api.ui.AppUiService;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SubscriptionService {

  private final MemberRepository memberRepository;
  private final StockRepository stockRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final AppUiService appUiService;

  @Transactional
  public void register(String userId, Time pushAt) {
    var member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("찾을 수 없는 멤버입니다."));
    member.registerSubscription(pushAt);
  }

  @Transactional
  public void subscribeStock(String userId, String ticker) {
    var member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("찾을 수 없는 멤버입니다."));
    var stock = stockRepository.findByTicker(ticker).orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));

    member.subscribeStock(stock);
    // Invalidate SDUI cache for this user so the dashboard reflects new subscriptions
    appUiService.invalidateForUser(userId);
  }

  public List<SubscribingStock> getSubscribingStocks(String userId) {
    var member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("찾을 수 없는 멤버입니다."));
    return member.getSubscribingStocks().stream().map(Stock::toSubscribingStock).toList();
  }


}
