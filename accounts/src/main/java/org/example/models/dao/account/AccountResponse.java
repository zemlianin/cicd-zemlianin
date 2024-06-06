package org.example.models.dao.account;

public class AccountResponse {
    private Long accountNumber;

    public AccountResponse(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }
}
