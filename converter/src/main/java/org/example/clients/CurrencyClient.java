package org.example.clients;

import org.example.models.CustomRetrySpec;
import org.example.models.dao.RatesResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class CurrencyClient {
    private final WebClient webClient;

    public CurrencyClient(@Qualifier("webClientWithTimeout") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<RatesResponse> GetRatesByCurrency(Mono<String> accessTokenMono) {
        var headers = new HttpHeaders();

        headers.setBearerAuth(accessTokenMono.block());

        return webClient
                .get()
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .retryWhen(new CustomRetrySpec(3,
                        Duration.ofMillis(50),
                        Duration.ofMillis(100),
                        Duration.ofMillis(150))
                );
    }
}
