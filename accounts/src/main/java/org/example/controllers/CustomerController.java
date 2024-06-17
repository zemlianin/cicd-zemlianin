package org.example.controllers;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.example.models.dao.BalanceResponse;
import org.example.models.dao.customer.CustomerRequest;
import org.example.models.dao.customer.CustomerResponse;
import org.example.models.enums.Currency;
import org.example.services.CustomerService;
import org.example.services.RateLimiterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final RateLimiterFactory rateLimiterFactory;
    private final ConcurrentMap<Long, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    @Autowired
    public CustomerController(CustomerService customerService, RateLimiterFactory rateLimiterFactory) {
        this.customerService = customerService;
        this.rateLimiterFactory = rateLimiterFactory;
    }

    @PostMapping()
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        var response = customerService.createCustomer(customerRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<BalanceResponse> getCustomerBalance(@PathVariable Long customerId,
                                                              @RequestParam Currency currency) {
        RateLimiter currentRateLimiter;

        if(rateLimiters.containsKey(customerId)){
            currentRateLimiter = rateLimiters.get(customerId);
        } else {
            currentRateLimiter = rateLimiterFactory.CreateNewRateLimiterByCustomer(customerId);
            rateLimiters.put(customerId, currentRateLimiter);
        }

        Supplier<ResponseEntity<BalanceResponse>> restrictedSupplier = RateLimiter.decorateSupplier(currentRateLimiter,
                () -> {
                    BalanceResponse response = customerService.getCustomerBalance(customerId, currency);
                    return ResponseEntity.ok(response);
                });

            return restrictedSupplier.get();
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> fallbackGetCustomerBalance(RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests - please try again later.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
