package org.example.models.dao.account;

import java.math.BigDecimal;

public class AccountBalanceResponse {
    private BigDecimal amount;
    private String currency;

    public AccountBalanceResponse(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
