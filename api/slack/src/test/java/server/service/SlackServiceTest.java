package server.service;

import api.subscriptions.domain.News.NewsDto;
import api.subscriptions.domain.Stock.StockDto;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import server.batch.job.NewsForUser;

@SpringBootTest
class SlackServiceTest {

  @Autowired
  private SlackService slackService;

  @Test
  void postStockInformation() throws SlackApiException, IOException {
    slackService.postStockInformation();
  }


  @Test
  void postStockInformation2() throws SlackApiException, IOException {

    List<NewsForUser> newsForUsers = List.of(new NewsForUser("U06BSRQ30PL",
        List.of(new NewsDto(new StockDto(1l, "nvidia", "nvda"), "요약"))
    ));

    slackService.postStockInformation(newsForUsers);
  }
}