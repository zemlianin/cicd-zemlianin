package org.example.controllers;

import org.example.models.NotificationPayload;
import org.example.models.dao.customer.CustomerRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class RateController {
    @GetMapping("/rates")
    public Integer getRate() {
        return 200;
    }

    @PostMapping("/notification")
    public void postNotification(@RequestBody NotificationPayload notificationPayload) {
        System.out.println("Catch request");
        System.out.println(notificationPayload.getCustomerId() + notificationPayload.getMessage());
    }
}

