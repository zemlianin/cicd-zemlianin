package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.clients.NotificationClient;
import org.example.configurations.AppSettings;
import org.example.models.NotificationPayload;
import org.example.models.entities.Outbox;
import org.example.repositories.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutboxProcessor {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final NotificationClient notificationClient;
    private final AppSettings appSettings;

    @Autowired
    public OutboxProcessor(OutboxRepository outboxRepository,
                           ObjectMapper objectMapper,
                           NotificationClient notificationClient,
                           AppSettings appSettings){
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
        this.notificationClient = notificationClient;
        this.appSettings = appSettings;
    }

    @Scheduled(fixedRate = 3000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void processOutbox() throws JsonProcessingException {
        List<Outbox> outboxEntries = outboxRepository.findUnprocessedPayloadNotification();

        for (Outbox outbox : outboxEntries) {
            var payload = objectMapper.readValue(outbox.getPayload(), NotificationPayload.class);

            System.out.println(appSettings.notificationUrl);
            notificationClient.postNotification(payload);

            outbox.setProcessed(true);
            outboxRepository.save(outbox);
        }
    }
}
