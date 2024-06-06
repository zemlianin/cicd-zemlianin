package org.example.models.dao;

import org.example.models.enums.Currency;

import java.math.BigDecimal;

public class BalanceResponse {

    private BigDecimal balance;
    private Currency currency;

    public BalanceResponse(BigDecimal balance, Currency currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

