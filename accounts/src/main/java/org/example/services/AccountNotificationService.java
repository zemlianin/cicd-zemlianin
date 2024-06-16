package org.example.services;

import org.example.models.AccountMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AccountNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyAccountChange(AccountMessage accountMessage) {
        System.out.println("--11---");
        System.out.println(accountMessage.getBalance());
        messagingTemplate.convertAndSend("/topic/accounts", accountMessage);
    }
}
