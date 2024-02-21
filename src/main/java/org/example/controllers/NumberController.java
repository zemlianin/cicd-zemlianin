package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.services.RandomGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resource/number")
public class NumberController {
    private static final Logger logger = LogManager.getLogger(NumberController.class);

    RandomGeneratorService randomGeneratorService;

    @Autowired
    public NumberController(RandomGeneratorService randomGeneratorService) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @GetMapping("/random")
    public Integer getNumber(HttpServletRequest request) {
        var ipAddress = request.getRemoteAddr();

        logger.info("Catch request to getting random number from user with IP " + ipAddress);
        return randomGeneratorService.getRandomNumber();
    }
}