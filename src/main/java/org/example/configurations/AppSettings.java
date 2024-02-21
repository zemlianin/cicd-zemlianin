package org.example.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppSettings {
    public static Integer randomNumber;

    @Value("${random_value.max}")
    public Integer maxOfRandomNumber;
}
