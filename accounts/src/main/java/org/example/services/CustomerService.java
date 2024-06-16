package org.example.services;

import org.example.clients.ConverterClient;
import org.example.clients.KeycloakClient;
import org.example.clients.grpc.GrpcConverterClient;
import org.example.configurations.AppSettings;
import org.example.models.entities.Account;
import org.example.models.entities.Customer;
import org.example.models.dao.BalanceResponse;
import org.example.models.dao.customer.CustomerRequest;
import org.example.models.dao.customer.CustomerResponse;
import org.example.models.enums.Currency;
import org.example.repositories.AccountRepository;
import org.example.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final ConverterClient converterClient;
    private final KeycloakClient keycloakClient;
    private final AppSettings appSettings;
    private final GrpcConverterClient grpcConverterClient;

    @Autowired
    public CustomerService(AccountRepository accountRepository,
                           CustomerRepository customerRepository,
                           ConverterClient converterClient,
                           KeycloakClient keycloakClient,
                           AppSettings appSettings,
                           GrpcConverterClient grpcConverterClient) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.converterClient = converterClient;
        this.keycloakClient = keycloakClient;
        this.grpcConverterClient = grpcConverterClient;
        this.appSettings = appSettings;
    }

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        if (customerRequest.getFirstName() == null || customerRequest.getLastName() == null || customerRequest.getBirthDay() == null) {
            throw new IllegalArgumentException("Missing required fields");
        }

        LocalDate birthDay = customerRequest.getBirthDay();
        LocalDate today = LocalDate.now();

        if (!birthDay.isBefore(today)) {
            throw new IllegalArgumentException("Birth date must be before today");
        }

        int age = Period.between(birthDay, today).getYears();

        if (age < 14 || age > 120) {
            throw new IllegalArgumentException("Age must be between 14 and 120 years");
        }

        Customer customer = new Customer();
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setBirthDay(customerRequest.getBirthDay());

        customer = customerRepository.save(customer);
        return new CustomerResponse(customer.getCustomerId());
    }

    public BalanceResponse getCustomerBalance(Long customerId, Currency currency) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        System.out.println("получил запрос на получение баланса для кастомера");

        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        var accounts = accountRepository.findByCustomerCustomerId(customerId);

        if (accounts.isEmpty()) {
            return new BalanceResponse(BigDecimal.ZERO, currency);
        }

        BigDecimal totalBalance = BigDecimal.ZERO;

        for (Account account : accounts) {
            System.out.println("вошел в цикл");

            if (account.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            if (!account.getCurrency().equals(currency)) {
                System.out.println("Начинаю перевед валют");

                BigDecimal convertedAmount;

                if(!appSettings.keepGrpcConnect) {
                    System.out.println("коннекчусь по грпс");

                    var accessToken = keycloakClient.auth(appSettings.resourceId, appSettings.keycloakClientSecret);
                    var monoConvertedAmount = converterClient.GetConvertedAmount(account.getCurrency(), currency, account.getBalance(), accessToken);
                    convertedAmount = monoConvertedAmount.block().getAmount();
                } else{
                    convertedAmount = grpcConverterClient.convert(account.getCurrency(), currency, account.getBalance());
                }

                totalBalance = totalBalance.add(convertedAmount);
            } else {
                totalBalance = totalBalance.add(account.getBalance());
            }
        }

        return new BalanceResponse(totalBalance.setScale(2, BigDecimal.ROUND_HALF_UP), currency);
    }
}