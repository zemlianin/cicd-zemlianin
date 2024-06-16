package org.example.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.configurations.AppSettings;
import org.example.grpc.ConverterProto;
import org.example.grpc.ConverterServiceGrpc;
import org.example.models.enums.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
public class GrpcConverterClient {
    private final ConverterServiceGrpc.ConverterServiceBlockingStub blockingStub;

    @Autowired
    public GrpcConverterClient(AppSettings appSettings) {
        var parts = appSettings.converterUrl.split(":");
        System.out.println(parts[0]);
        System.out.println(parts[1]);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(parts[0], Integer.getInteger(parts[1]))
                .usePlaintext()
                .build();
        blockingStub = ConverterServiceGrpc.newBlockingStub(channel);
    }

    public BigDecimal convert(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
        try {
            ConverterProto.ConvertRequest request = ConverterProto.ConvertRequest.newBuilder()
                    .setFromCurrency(fromCurrency.toString())
                    .setToCurrency(toCurrency.toString())
                    .setAmount(amount.doubleValue())
                    .build();
            System.out.println("Запрос создан");
            ConverterProto.ConvertResponse response = blockingStub.convert(request);
            System.out.println("конвертация прошла");
            return BigDecimal.valueOf(response.getConvertedAmount());
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }

    }
}

