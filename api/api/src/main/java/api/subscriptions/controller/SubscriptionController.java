package api.subscriptions.controller;

import api.subscriptions.controller.request.RegisterSubscription;
import api.subscriptions.controller.request.SubscribeStock;
import api.subscriptions.controller.response.SubscribingStock;
import api.subscriptions.service.SubscriptionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @PostMapping("/register")
  public void register(@RequestParam String email, @RequestBody @Valid RegisterSubscription registerSubscription) {

    subscriptionService.register(email, registerSubscription.pushAt());
  }

  @PostMapping
  public void subscribeStock(@RequestParam String email, @RequestBody @Valid SubscribeStock subscribeStock) {
    subscriptionService.subscribeStock(email, subscribeStock.ticker());
  }

  @GetMapping("/subscribingStocks")
  public ResponseEntity<List<SubscribingStock>> getSubscribingStocks(@RequestParam String email) {
    return ResponseEntity.ok(subscriptionService.getSubscribingStocks(email));
  }
}
