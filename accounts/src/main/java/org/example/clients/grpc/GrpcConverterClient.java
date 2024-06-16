package org.example.clients.grpc;

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
    private ConverterServiceGrpc.ConverterServiceBlockingStub blockingStub;

    private AppSettings appSettings;
    @Autowired
    public GrpcConverterClient(AppSettings appSettings){
        this.appSettings = appSettings;
    }

    public GrpcConverterClient() {
        var parts = appSettings.converterUrl.split(":");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(parts[0], Integer.getInteger(parts[1]))
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
        System.out.println("Запрос создан");
        ConverterProto.ConvertResponse response = blockingStub.convert(request);
        System.out.println("конвертация прошла");
        return BigDecimal.valueOf(response.getConvertedAmount());
    }
}

