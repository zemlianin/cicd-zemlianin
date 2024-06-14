package org.example.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.ConverterProto;
import org.example.grpc.ConverterServiceGrpc;
import org.example.models.enums.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class GrpcConverterClient {
    private final ConverterServiceGrpc.ConverterServiceBlockingStub blockingStub;

    public GrpcConverterClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        blockingStub = ConverterServiceGrpc.newBlockingStub(channel);
    }

    public BigDecimal convert(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
        ConverterProto.ConvertRequest request = ConverterProto.ConvertRequest.newBuilder()
                .setFromCurrency(fromCurrency.toString())
                .setToCurrency(toCurrency.toString())
                .setAmount(amount.doubleValue())
                .build();
        ConverterProto.ConvertResponse response = blockingStub.convert(request);
        return BigDecimal.valueOf(response.getConvertedAmount());
    }
}

