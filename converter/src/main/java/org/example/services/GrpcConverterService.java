package org.example.services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.grpc.ConverterProto;
import org.example.grpc.ConverterServiceGrpc;
import org.example.models.enums.Currency;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@GrpcService
public class GrpcConverterService extends ConverterServiceGrpc.ConverterServiceImplBase {
    private final ConvertService convertService;

    @Autowired
    public GrpcConverterService(ConvertService convertService){
        this.convertService = convertService;
    }

    @Override
    public void convert(ConverterProto.ConvertRequest request, StreamObserver<ConverterProto.ConvertResponse> responseObserver) {
        System.out.println("прошла авторизация");
        BigDecimal convertedAmount = convertService.convert(Currency.fromValue(request.getFromCurrency()),
                Currency.fromValue(request.getToCurrency()),
                BigDecimal.valueOf(request.getAmount()));
        ConverterProto.ConvertResponse response = ConverterProto.ConvertResponse.newBuilder()
                .setConvertedAmount(convertedAmount.doubleValue())
                .build();
        System.out.println("направляю ответ");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println("Ответ направлен");
    }
}

