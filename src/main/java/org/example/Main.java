package org.example;

import org.example.configurations.AppSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var random = new Random();

        AppSettings.randomNumber = random.nextInt();

        SpringApplication.run(Main.class, args);
    }
}