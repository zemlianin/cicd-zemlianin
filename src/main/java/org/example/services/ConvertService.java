package org.example.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.clients.CurrencyClient;
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

    @Autowired
    public ConvertService(CurrencyClient currencyClient) {
        this.currencyClient = currencyClient;
    }

    public Mono<BigDecimal> convert(Currency currencyFrom, Currency currencyTo, BigDecimal amount) {
        Mono<RatesResponse> ratesMono = currencyClient.GetRatesByCurrency();

        return ratesMono.map(ratesResponse -> convertBase(ratesResponse, currencyFrom, currencyTo, amount))
                .onErrorResume(error -> {
                    logger.error(error.getMessage());
                    return Mono.error(error);
                });
    }

    public BigDecimal convertBase(RatesResponse rates,
                                  Currency currencyFrom,
                                  Currency currencyTo,
                                  BigDecimal amount) {

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
            return rates.getRates().get(currencyFrom.toString()).multiply(amount);
        } else if (currencyFrom == Currency.RUB) {
            return amount.divide(rates.getRates().get(currencyTo.toString()), 2, RoundingMode.HALF_EVEN);
        } else {
            var sumInRub = rates.getRates().get(currencyFrom.toString()).multiply(amount);
            return sumInRub.divide(rates.getRates().get(currencyTo.toString()), 2, RoundingMode.HALF_EVEN);
        }
    }
}
