package org.example.controllers;

import org.example.models.dao.RatesResponse;
import org.example.models.enums.Currency;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rates")
public class RateController {
    @GetMapping
    public RatesResponse getRate() {
        RatesResponse response = new RatesResponse();
        response.setBase(Currency.RUB); // Установка базовой валюты
        Map<String, BigDecimal> rates = new HashMap<>();

        rates.put("USD", BigDecimal.valueOf(90.62)); // Добавление курса для USD
        rates.put("EUR", BigDecimal.valueOf(102.48)); // Добавление курса для USD
        rates.put("GBP", BigDecimal.valueOf(120.51)); // Добавление курса для RUB (обычно 1)
        rates.put("RUB", BigDecimal.ONE); // Добавление курса для RUB (обычно 1)
        rates.put("CNY", BigDecimal.valueOf(12.23)); // Добавление курса для RUB (обычно 1)

        response.setRates(rates);
        return response;
    }
}
