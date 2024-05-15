package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.dao.CurrencyResponse;
import org.example.models.enums.Currency;
import org.example.services.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping()
    public ResponseEntity<CurrencyResponse> getNumber(HttpServletRequest request,
                                                      @RequestParam(value = "from", required = false) String from,
                                                      @RequestParam(value = "to", required = false) String to,
                                                      @RequestParam(value = "amount", required = false) BigDecimal amount) {
        try {
            var currencyFrom = stringToCurrency(from);
            var currencyTo = stringToCurrency(to);

            return new ResponseEntity<>(new CurrencyResponse(currencyTo,
                    convertService.convert(currencyFrom, currencyTo, amount)
                    ),
                    HttpStatus.OK);
        } catch (InvalidParameterException e) {
            var response = new CurrencyResponse();
            response.setMessage("Отрицательная сумма");

            return new ResponseEntity<>(response, HttpStatus.valueOf(400));

        } catch (IllegalArgumentException ex) {
            var response = new CurrencyResponse();
            response.setMessage("Валюта " + ex.getMessage() + " недоступна");

            return new ResponseEntity<>(response, HttpStatus.valueOf(400));

        } catch (Exception e) {
            var response = new CurrencyResponse();
            System.out.println(from);
            System.out.println(to);
            System.out.println(amount);

            response.setMessage("Unknown Error");

            return new ResponseEntity<>(response, HttpStatus.valueOf(500));
        }
    }

    private Currency stringToCurrency(String name){
        var result = Currency.fromValue(name);

        if (result == null){
            throw new IllegalArgumentException(name);
        }

        return result;
    }
}