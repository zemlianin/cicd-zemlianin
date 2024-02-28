package org.example.clients;

import org.example.configurations.AppSettings;
import org.example.models.dao.RatesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class CurrencyClient {
    private final WebClient webClient;
    private final AppSettings appSettings;

    public CurrencyClient(WebClient webClient, AppSettings appSettings) {
        this.webClient = webClient;
        this.appSettings = appSettings;
    }

    public Mono<RatesResponse> GetRatesByCurrency() {
        return webClient
                .get()
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .retryWhen(Retry.fixedDelay(appSettings.retryAttempts,
                        Duration.ofMillis(appSettings.retryDelayMillis)));
    }
}
