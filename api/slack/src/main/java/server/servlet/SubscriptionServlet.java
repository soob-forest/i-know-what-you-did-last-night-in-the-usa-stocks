package server.servlet;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jakarta_servlet.SlackAppServlet;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/command")
public class SubscriptionServlet extends SlackAppServlet {

  public SubscriptionServlet(App slackApp) {
    super(slackApp);
  }
}
