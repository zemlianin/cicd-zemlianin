package org.example.models.dao;

import java.math.BigDecimal;

public class TopUpRequest {
    private BigDecimal amount;

    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
