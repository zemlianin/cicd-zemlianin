package org.example.services;

import org.example.clients.ConverterClient;
import org.example.clients.KeycloakClient;
import org.example.clients.grpc.GrpcConverterClient;
import org.example.configurations.AppSettings;
import org.example.models.entities.Account;
import org.example.models.entities.Customer;
import org.example.models.dao.*;
import org.example.models.dao.account.AccountBalanceResponse;
import org.example.models.dao.account.AccountRequest;
import org.example.models.dao.account.AccountResponse;
import org.example.repositories.AccountRepository;
import org.example.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final ConverterClient converterClient;
    private final KeycloakClient keycloakClient;
    private final AppSettings appSettings;
    private final GrpcConverterClient grpcConverterClient;

    @Autowired
    public AccountService(CustomerRepository customerRepository,
                          AccountRepository accountRepository,
                          ConverterClient converterClient,
                          KeycloakClient keycloakClient,
                          AppSettings appSettings,
                          GrpcConverterClient grpcConverterClient
    ){
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.converterClient = converterClient;
        this.keycloakClient = keycloakClient;
        this.appSettings = appSettings;
        this.grpcConverterClient = grpcConverterClient;
    }
    public AccountResponse createAccount(AccountRequest accountRequest) {
        if (accountRequest.getCustomerId() == null || accountRequest.getCurrency() == null) {
            throw new IllegalArgumentException("Missing required fields");
        }

        var currency = accountRequest.getCurrency();

        Optional<Customer> customerOpt = customerRepository.findById(accountRequest.getCustomerId());

        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Optional<Account> existingAccount = accountRepository.findByCustomerCustomerIdAndCurrency(
                accountRequest.getCustomerId(), currency);

        if (existingAccount.isPresent()) {
            throw new IllegalArgumentException("Account in this currency already exists for the customer");
        }

        Account account = new Account();
        account.setCustomer(customerOpt.get());
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(account);
        return new AccountResponse(savedAccount.getAccountId());
    }

    public AccountBalanceResponse getAccountBalance(Long accountNumber) {
        Optional<Account> accountOpt = accountRepository.findById(accountNumber);

        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        return new AccountBalanceResponse(account.getBalance(), account.getCurrency().toString());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void topUpAccount(Long accountNumber, TopUpRequest topUpRequest) {
        if (topUpRequest.getAmount() == null) {
            throw new IllegalArgumentException("Missing required field: amount");
        }

        if (topUpRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        account.setBalance(account.getBalance().add(topUpRequest.getAmount()));
        accountRepository.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transferFunds(TransferRequest transferRequest) {
        if (transferRequest.getAmountInSenderCurrency() == null) {
            throw new IllegalArgumentException("Missing required field: amountInSenderCurrency");
        }

        if (transferRequest.getAmountInSenderCurrency().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Optional<Account> senderAccountOpt = accountRepository.findById(transferRequest.getSenderAccount());
        Optional<Account> receiverAccountOpt = accountRepository.findById(transferRequest.getReceiverAccount());

        if (senderAccountOpt.isEmpty() || receiverAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account senderAccount = senderAccountOpt.get();
        Account receiverAccount = receiverAccountOpt.get();

        if (senderAccount.getBalance().compareTo(transferRequest.getAmountInSenderCurrency()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        BigDecimal amountInReceiverCurrency;

        if (senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            amountInReceiverCurrency = transferRequest.getAmountInSenderCurrency();
        } else {
            amountInReceiverCurrency = grpcConverterClient.convert(
                    senderAccount.getCurrency(),
                    receiverAccount.getCurrency(),
                    transferRequest.getAmountInSenderCurrency());
            /*var accessToken = keycloakClient.auth(appSettings.resourceId, appSettings.keycloakClientSecret);
            amountInReceiverCurrency = converterClient.GetConvertedAmount(
                    senderAccount.getCurrency(),
                    receiverAccount.getCurrency(),
                    transferRequest.getAmountInSenderCurrency(),
                    accessToken
            ).block().getAmount();*/
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(transferRequest.getAmountInSenderCurrency()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amountInReceiverCurrency));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
    }
}
