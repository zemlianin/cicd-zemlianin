package org.example.models;

import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class CustomRetrySpec extends Retry {
    private final int retries;
    private final Duration firstTime;
    private final Duration secondTime;
    private final Duration restTime;

    public CustomRetrySpec(int retries, Duration firstTime, Duration secondTime, Duration restTime) {
        this.retries = retries;
        this.firstTime = firstTime;
        this.secondTime = secondTime;
        this.restTime = restTime;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> retrySignals) {
        return retrySignals.flatMap(this::getRetry);
    }

    private Mono<Long> getRetry(Retry.RetrySignal rs) {
        if (rs.totalRetries() < retries) {
            Duration delay;

            if (rs.totalRetries() == 0) {
                delay = firstTime;
            } else if (rs.totalRetries() == 1) {
                delay = secondTime;
            } else {
                delay = restTime;
            }

            return Mono.delay(delay)
                    .thenReturn(rs.totalRetries());
        } else {
            throw Exceptions.propagate(rs.failure());
        }
    }
}
