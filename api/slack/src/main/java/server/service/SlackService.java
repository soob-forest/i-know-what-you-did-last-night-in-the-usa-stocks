package server.service;

import api.members.servic.MemberService;
import api.subscriptions.service.SubscriptionService;
import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.bolt.App;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import server.batch.job.NewsForUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {

  private final App slackApp;
  private final MethodsClient slackClient;
  private final MemberService memberService;
  private final SubscriptionService subscriptionService;

  @Bean
  public App register() {

    slackApp.command("/등록", ((slashCommandRequest, context) -> {
      try {
        SlashCommandPayload payload = slashCommandRequest.getPayload();
        var member = memberService.findByUserId(payload.getUserId());
        if (member.isEmpty()) {
          memberService.join(payload.getUserId());
        }

        subscriptionService.subscribeStock(payload.getUserId(), payload.getText());

        return context.ack(payload.getText() + "을 구독합니다.");
      } catch (Exception e) {
        return context.ack(e.getMessage());
      }
    }));
    return slackApp;
  }

  public void postStockInformation() throws SlackApiException, IOException {

    var conversationsOpenResponse = slackClient.conversationsOpen(ConversationsOpenRequest.builder().users(List.of("U06BSRQ30PL")).build());
    var response = slackClient.chatPostMessage(
        ChatPostMessageRequest.builder().channel(conversationsOpenResponse.getChannel().getId()).text("test").build());

    System.out.println(response);
  }

  public void postStockInformation(List<NewsForUser> newsForUsers) throws SlackApiException, IOException {

    for (var newsForUser : newsForUsers) {
      var conversationsOpenResponse = slackClient.conversationsOpen(ConversationsOpenRequest.builder().users(List.of(newsForUser.userId())).build());

      var newsSummary = String.join("\n=============================\n",
          newsForUser.news().stream().map(newsDto -> String.format("%s에 한 밤 중에 일어난 일\n\n%s", newsDto.stock().name(), newsDto.summary())).toList());

      var response = slackClient.chatPostMessage(
          ChatPostMessageRequest.builder().channel(conversationsOpenResponse.getChannel().getId()).text(newsSummary).build());
      System.out.println(response);
    }


  }
}
