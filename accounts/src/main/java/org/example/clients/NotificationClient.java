package org.example.clients;

import org.example.configurations.AppSettings;
import org.example.models.NotificationPayload;
import org.example.models.dao.CurrencyResponse;
import org.example.models.enums.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;

@Component
public class NotificationClient {
    private final WebClient webClient;

    private final AppSettings appSettings;

    @Autowired
    public NotificationClient(@Qualifier("notificationClient") WebClient webClient, AppSettings appSettings) {
        this.webClient = webClient;
        this.appSettings = appSettings;
    }

    public Mono<Void> postNotification(NotificationPayload notificationPayload) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/notification")
                        .build())
                .body(Mono.just(notificationPayload), NotificationPayload.class)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(Retry.fixedDelay(appSettings.retryAttempts,
                        Duration.ofMillis(appSettings.retryDelayMillis)));
    }
}