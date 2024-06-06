package org.example.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppSettings {

    @Value("${random_value.max}")
    public Integer maxOfRandomNumber;

    @Value("${rate_client.url}")
    public String rateUrl;

    @Value("${client.timeout}")
    public int timeout;

    @Value("${rate_client.retry_attempts}")
    public int retryAttempts;

    @Value("${rate_client.retry_delay_millis}")
    public int retryDelayMillis;

    @Value("${keycloak.realm}")
    public String realmName;

    @Value("${keycloak.url}")
    public String keycloakBaseUrl;

    @Value("${keycloak.client_secret}")
    public String keycloakClientSecret;
}
