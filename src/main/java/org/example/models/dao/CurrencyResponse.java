package org.example.models.dao;

import org.example.models.enums.Currency;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class CurrencyResponse {
    private Currency currency;
    private BigDecimal amount;
    private String message;

    public CurrencyResponse() {
    }

    public CurrencyResponse(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
