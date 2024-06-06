package org.example.models.dao;

import java.math.BigDecimal;

public class TransferRequest {
    private Long receiverAccount;
    private Long senderAccount;
    private BigDecimal amountInSenderCurrency;

    public Long getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Long receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public Long getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Long senderAccount) {
        this.senderAccount = senderAccount;
    }

    public BigDecimal getAmountInSenderCurrency() {
        return amountInSenderCurrency;
    }

    public void setAmountInSenderCurrency(BigDecimal amountInSenderCurrency) {
        this.amountInSenderCurrency = amountInSenderCurrency;
    }
}
