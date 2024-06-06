package org.example.clients;

import org.example.configurations.AppSettings;
import org.example.models.dao.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class KeycloakClient {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakClient.class);

    private final WebClient keycloakClient;

    private final AppSettings appSettings;

    private final String authEndpoint;

    @Autowired
    public KeycloakClient(@Qualifier("keycloakClient") WebClient keycloakClient, AppSettings appSettings) {
        this.keycloakClient = keycloakClient;
        this.appSettings = appSettings;
        authEndpoint = "/realms/" + appSettings.realmName + "/protocol/openid-connect/token";
    }

    public Mono<String> auth(String clientId, String clientSecret) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var requestBody = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";

        return keycloakClient
                .post()
                .uri(authEndpoint)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .map(response -> {
                    if (response == null) {
                        logger.error("Received null response from Keycloak");
                        throw new RuntimeException("Received null response from Keycloak");
                    }

                    if (response.getAccessToken() == null) {
                        logger.error("No access token in response: {}", response);
                        throw new RuntimeException("No access token in response");
                    }
                    return response.getAccessToken();
                })
                .doOnError(error -> logger.error("Error during authentication with Keycloak", error))
                .retryWhen(Retry.fixedDelay(appSettings.retryAttempts, Duration.ofMillis(appSettings.retryDelayMillis)));
    }
}