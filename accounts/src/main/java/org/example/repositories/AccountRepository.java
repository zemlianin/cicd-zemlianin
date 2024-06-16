package org.example.repositories;

import org.example.models.entities.Account;
import org.example.models.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerCustomerId(Long customerId);

    Optional<Account> findByCustomerCustomerIdAndCurrency(Long customerId, Currency currency);
}

