package api.subscriptions.controller.request;

import jakarta.validation.constraints.NotBlank;

public record SubscribeStock(@NotBlank(message = "ticker가 필요합니다.") String ticker) {

}
