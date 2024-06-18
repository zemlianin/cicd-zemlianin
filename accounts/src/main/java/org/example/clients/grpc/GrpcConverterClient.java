package org.example.clients.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.configurations.AppSettings;
import org.example.grpc.ConverterProto;
import org.example.grpc.ConverterServiceGrpc;
import org.example.models.enums.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GrpcConverterClient {
    private final ConverterServiceGrpc.ConverterServiceBlockingStub blockingStub;

    @Autowired
    public GrpcConverterClient(AppSettings appSettings) {
        var parts = appSettings.converterUrl.split(":");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(parts[0], Integer.parseInt(parts[1]))
                .usePlaintext()
                .build();
        blockingStub = ConverterServiceGrpc.newBlockingStub(channel);
    }

    @CircuitBreaker(name = "grpcConverter", fallbackMethod = "fallbackConvert")
    public BigDecimal convert(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
            ConverterProto.ConvertRequest request = ConverterProto.ConvertRequest.newBuilder()
                    .setFromCurrency(fromCurrency.toString())
                    .setToCurrency(toCurrency.toString())
                    .setAmount(amount.doubleValue())
                    .build();
            ConverterProto.ConvertResponse response = blockingStub.convert(request);
            return BigDecimal.valueOf(response.getConvertedAmount());
    }

    private BigDecimal fallbackConvert(Currency fromCurrency, Currency toCurrency, BigDecimal amount, Throwable t) {
        return BigDecimal.ZERO;
    }
}