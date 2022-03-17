package com.inetum.warehouse.controller;

import com.inetum.warehouse.exception.EmptyOrderException;
import com.inetum.warehouse.model.AbstractPurchase;
import com.inetum.warehouse.service.PurchasedProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchasedProductsController {

    private final PurchasedProductsService purchasedProductsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public AbstractPurchase createOrder(@RequestBody @NotEmpty Map<String, Long> orderedProduct) {
        return purchasedProductsService.validateOrder(orderedProduct);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidException(HttpMessageNotReadableException e) {
        return new ResponseEntity("Give the correct values!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleValidException(NumberFormatException e) {
        return new ResponseEntity("Give the correct values!", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmptyOrderException.class)
    public ResponseEntity<String> handleException(EmptyOrderException e) {
        return new ResponseEntity("Min one product in order!", HttpStatus.BAD_REQUEST);
    }

}
