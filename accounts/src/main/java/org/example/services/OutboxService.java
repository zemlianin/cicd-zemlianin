package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.NotificationPayload;
import org.example.models.entities.Account;
import org.example.models.entities.Outbox;
import org.example.repositories.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public OutboxService(OutboxRepository outboxRepository,
                         ObjectMapper objectMapper){
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    public void addPayloadNotification(Account account, BigDecimal difference) throws JsonProcessingException {
        var outbox = new Outbox();
        outbox.setAggregateType("PayloadNotification");
        outbox.setAggregateId(account.getAccountId());
        outbox.setType("Notification");
        var notificationPayload = new NotificationPayload();

        String operationSymbol = "+";

        if(difference.compareTo(BigDecimal.ZERO) <= 0){
            operationSymbol = "";
        }

        notificationPayload.setMessage("Счет " + account.getAccountId() +
                ". Операция: "+ operationSymbol + difference + ". Баланс: " + account.getBalance());
        notificationPayload.setCustomerId(account.getCustomer().getCustomerId());
        outbox.setPayload(objectMapper.writeValueAsString(notificationPayload));
        outboxRepository.save(outbox);
    }
}
