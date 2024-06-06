package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.dao.CurrencyResponse;
import org.example.models.dao.ErrorCurrencyResponse;
import org.example.models.enums.Currency;
import org.example.services.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

@RestController
@RequestMapping("/convert")
public class ConvertController {
    private static final Logger logger = LogManager.getLogger(ConvertController.class);

    ConvertService convertService;

    @Autowired
    public ConvertController(ConvertService convertService) {
        this.convertService = convertService;
    }

    @GetMapping("/ping")
    public int getNumber() {
        return 200;
    }

    @GetMapping()
    public ResponseEntity<CurrencyResponse> getNumber(HttpServletRequest request,
                                                      @RequestParam(value = "from", required = false) String from,
                                                      @RequestParam(value = "to", required = false) String to,
                                                      @RequestParam(value = "amount", required = false) BigDecimal amount) {
        var currencyFrom = stringToCurrency(from);
        var currencyTo = stringToCurrency(to);

        return new ResponseEntity<>(new CurrencyResponse(currencyTo,
                convertService.convert(currencyFrom, currencyTo, amount)
        ),
                HttpStatus.OK);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorCurrencyResponse> handleNotFoundException(InvalidParameterException ex) {
        var response = new ErrorCurrencyResponse();
        response.setMessage("Отрицательная сумма");

        return new ResponseEntity<>(response, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorCurrencyResponse> handleGeneralException(IllegalArgumentException ex) {
        var response = new ErrorCurrencyResponse();
        response.setMessage("Валюта " + ex.getMessage() + " недоступна");

        return new ResponseEntity<>(response, HttpStatus.valueOf(400));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorCurrencyResponse> handleGeneralException(Exception ex) {
        var response = new ErrorCurrencyResponse();
        System.out.println(ex.getMessage());

        response.setMessage("Unknown Error");

        return new ResponseEntity<>(response, HttpStatus.valueOf(500));
    }

    private Currency stringToCurrency(String name) {
        var result = Currency.fromValue(name);

        if (result == null) {
            throw new IllegalArgumentException(name);
        }

        return result;
    }
}