package org.example.controllers;

import org.example.models.dao.BalanceResponse;
import org.example.models.dao.customer.CustomerRequest;
import org.example.models.dao.customer.CustomerResponse;
import org.example.models.enums.Currency;
import org.example.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        var response = customerService.createCustomer(customerRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<BalanceResponse> getCustomerBalance(@PathVariable Long customerId,
                                                              @RequestParam Currency currency) {
        BalanceResponse response = customerService.getCustomerBalance(customerId, currency);
        return ResponseEntity.ok(response);
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
