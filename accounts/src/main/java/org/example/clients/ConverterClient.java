package org.example.clients;

import org.example.configurations.AppSettings;
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
public class ConverterClient {
    private final WebClient webClient;

    private final AppSettings appSettings;

    @Autowired
    public ConverterClient(@Qualifier("converterClient") WebClient webClient, AppSettings appSettings) {
        this.webClient = webClient;
        this.appSettings = appSettings;
    }

    public Mono<CurrencyResponse> GetConvertedAmount(Currency from, Currency to, BigDecimal amount, Mono<String> accessTokenMono) {
        var headers = new HttpHeaders();

        headers.setBearerAuth(accessTokenMono.block());

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/convert")
                        .queryParam("from", from.toString())
                        .queryParam("to", to.toString())
                        .queryParam("amount", amount)
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(CurrencyResponse.class)
                .retryWhen(Retry.fixedDelay(appSettings.retryAttempts,
                        Duration.ofMillis(appSettings.retryDelayMillis)));
    }
}
