package org.example.models.dao.customer;

public class CustomerResponse {

    private Long customerId;

    public CustomerResponse(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
