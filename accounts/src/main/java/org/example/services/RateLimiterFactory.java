package org.example.services;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class RateLimiterFactory {
    public RateLimiter CreateNewRateLimiterByCustomer(Long customerId){
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(0))
                .build();
        return RateLimiter.of(customerId.toString(), config);
    }
}
