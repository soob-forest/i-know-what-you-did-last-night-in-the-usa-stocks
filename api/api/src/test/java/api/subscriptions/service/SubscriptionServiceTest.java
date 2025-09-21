package api.subscriptions.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import api.members.domain.Member;
import api.members.domain.MemberRepository;
import api.subscriptions.domain.Stock;
import api.subscriptions.domain.StockRepository;
import api.subscriptions.domain.SubscriptionRepository;
import api.ui.AppUiService;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriptionServiceTest {

  MemberRepository memberRepository = mock(MemberRepository.class);
  StockRepository stockRepository = mock(StockRepository.class);
  SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
  AppUiService appUiService = mock(AppUiService.class);

  SubscriptionService service;

  @BeforeEach
  void setup() {
    service = new SubscriptionService(memberRepository, stockRepository, subscriptionRepository, appUiService);
  }

  @Test
  void subscribeStock_invalidatesCacheForUser() {
    var member = Member.builder().userId("U_TEST").build();
    member.registerSubscription(Time.valueOf(LocalTime.of(9,0,0)));
    when(memberRepository.findByUserId("U_TEST")).thenReturn(Optional.of(member));
    when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(mock(Stock.class)));

    service.subscribeStock("U_TEST", "AAPL");

    verify(appUiService).invalidateForUser("U_TEST");
  }
}

