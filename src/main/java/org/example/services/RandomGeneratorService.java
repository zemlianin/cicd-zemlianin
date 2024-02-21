package org.example.services;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.configurations.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RandomGeneratorService {
    private final Integer randomNumber;

    @Autowired
    public RandomGeneratorService(AppSettings appSettings){
        randomNumber = AppSettings.randomNumber % appSettings.maxOfRandomNumber;
    }
    public Integer getRandomNumber(){

        return randomNumber;
    }
}
