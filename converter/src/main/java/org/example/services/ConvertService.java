package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.clients.CurrencyClient;
import org.example.clients.KeycloakClient;
import org.example.configurations.AppSettings;
import org.example.configurations.security.JwtConverterProperties;
import org.example.models.dao.RatesResponse;
import org.example.models.enums.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@Service
public class ConvertService {
    private static final Logger logger = LogManager.getLogger(ConvertService.class);
    CurrencyClient currencyClient;
    KeycloakClient keycloakClient;
    JwtConverterProperties jwtConverterProperties;
    AppSettings appSettings;

    @Autowired
    public ConvertService(CurrencyClient currencyClient,
                          KeycloakClient keycloakClient,
                          JwtConverterProperties jwtConverterProperties,
                          AppSettings appSettings) {
        this.currencyClient = currencyClient;
        this.keycloakClient = keycloakClient;
        this.jwtConverterProperties = jwtConverterProperties;
        this.appSettings = appSettings;
    }

    public BigDecimal convert(Currency currencyFrom, Currency currencyTo, BigDecimal amount) {
        var objectMapper = new ObjectMapper();

        var accessResponse = keycloakClient.auth(jwtConverterProperties.getResourceId(),appSettings.keycloakClientSecret);
        System.out.println("прошла авторизация");
        var jsonString = currencyClient.GetRatesByCurrency(accessResponse).block();
        System.out.println("Получил рейтс");
        RatesResponse ratesMono = null;
        System.out.println(jsonString);
        try {
            ratesMono = objectMapper.readValue(jsonString, RatesResponse.class);
        } catch (JsonProcessingException e) {
            System.out.println("Error of parsing");
        }

        if(ratesMono == null){
            System.out.println("Rates is null");
        }

        return convertBase(ratesMono, currencyFrom, currencyTo, amount);
    }
    public BigDecimal convertBase(RatesResponse rates,
                                  Currency currencyFrom,
                                  Currency currencyTo,
                                  BigDecimal amount) {
        System.out.println(rates.getRates());

        if (!rates.getRates().containsKey(currencyFrom.toString())) {
            throw new IllegalArgumentException(currencyFrom.toString());
        }

        if (!rates.getRates().containsKey(currencyTo.toString())) {
            throw new IllegalArgumentException(currencyTo.toString());
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidParameterException();
        }

        if (currencyTo == Currency.RUB) {
            return rates.getRates().get(currencyFrom.toString()).multiply(amount).setScale(2, RoundingMode.HALF_EVEN);
        } else if (currencyFrom == Currency.RUB) {
            return amount.divide(rates.getRates().get(currencyTo.toString()), 2, RoundingMode.HALF_EVEN);
        } else {
            var sumInRub = rates.getRates().get(currencyFrom.toString()).multiply(amount);
            return sumInRub.divide(rates.getRates().get(currencyTo.toString()), 2, RoundingMode.HALF_EVEN);
        }
    }
}
