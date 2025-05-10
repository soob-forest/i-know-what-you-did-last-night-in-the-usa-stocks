package server.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.methods.MethodsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SlackAppConfig {

  @Bean
  public App slackApp() {
    AppConfig appConfig = AppConfig.builder().singleTeamBotToken(process.env['SLACK_BOT_TOKEN'] )
        .signingSecret(process.env['SLACK_BOT_SECRET']).build();
    App app = new App(appConfig);
    app.command("/hello", (req, ctx) -> ctx.ack("Hi there!"));

    return app;
  }

  @Bean
  public MethodsClient slackClient(App slackApp) {
    return slackApp.client();
  }
}
