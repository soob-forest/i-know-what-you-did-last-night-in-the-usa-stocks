package api.subscriptions.controller.request;

import java.sql.Time;

public record RegisterSubscription(
    Time pushAt) {

}
