package server.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import api.subscriptions.domain.News.NewsDto;
import api.subscriptions.domain.NewsLink.LinkDto;
import api.subscriptions.domain.Stock.StockDto;
import com.slack.api.bolt.App;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.batch.job.NewsForUser;

@ExtendWith(MockitoExtension.class)
class SlackServiceTest {

  @Mock App slackApp;
  @Mock MethodsClient slackClient;
  @Mock api.members.servic.MemberService memberService;
  @Mock api.subscriptions.service.SubscriptionService subscriptionService;

  @InjectMocks SlackService slackService;

  @BeforeEach
  void setup() throws IOException, SlackApiException {
    ConversationsOpenResponse conv = mock(ConversationsOpenResponse.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
    when(conv.getChannel().getId()).thenReturn("C_TEST");
    when(slackClient.conversationsOpen(any(ConversationsOpenRequest.class))).thenReturn(conv);
    when(slackClient.chatPostMessage(any(ChatPostMessageRequest.class))).thenReturn(mock(ChatPostMessageResponse.class));
  }

  @Test
  void postStockInformation2() throws SlackApiException, IOException {
    List<NewsForUser> newsForUsers = List.of(new NewsForUser("U_TEST",
        List.of(new NewsDto(new StockDto(1L, "nvidia", "NVDA"), "요약", List.<LinkDto>of()))
    ));

    slackService.postStockInformation(newsForUsers);
  }
}
