package org.example.clients;

import org.example.configurations.AppSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class CurrencyClient {
    private final WebClient webClient;

    private final AppSettings appSettings;

    public CurrencyClient(@Qualifier("webClientWithTimeout") WebClient webClient, AppSettings appSettings) {
        this.webClient = webClient;
        this.appSettings = appSettings;
    }

    public Mono<String> GetRatesByCurrency(Mono<String> accessTokenMono) {
        var headers = new HttpHeaders();

        headers.setBearerAuth(accessTokenMono.block());

        return webClient
                .get()
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(appSettings.retryAttempts,
                        Duration.ofMillis(appSettings.retryDelayMillis)));
    }
}
