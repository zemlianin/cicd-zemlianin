package org.example.models;

public class NotificationPayload {
    private Long customerId;
    private String message;

    // геттеры и сеттеры

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

