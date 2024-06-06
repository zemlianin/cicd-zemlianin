package org.example.models.dao.account;

import org.example.models.enums.Currency;

public class AccountRequest {
    private Long customerId;
    private Currency currency;

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}