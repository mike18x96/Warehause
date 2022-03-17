package com.inetum.warehouse.controller;

import com.inetum.warehouse.exception.NotEnoughAmountException;
import com.inetum.warehouse.model.AbstractPurchase;
import com.inetum.warehouse.model.UnsuccessfulPurchase;
import com.inetum.warehouse.service.PurchasedProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
    @Transactional(rollbackFor = NotEnoughAmountException.class)
    @Validated
    public AbstractPurchase responsePurchase(@RequestBody @NotEmpty Map<String, Long> orderedProduct) {
        try {
            AbstractPurchase response = purchasedProductsService.createOrder(orderedProduct);

            if(purchasedProductsService.returnMissingProduct().isEmpty()){
                return response;
            }else {
                throw new NotEnoughAmountException();
            }
        } catch (NotEnoughAmountException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new UnsuccessfulPurchase(false, purchasedProductsService.returnMissingProduct());
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidException(HttpMessageNotReadableException e) {
        return new ResponseEntity("Incomplete object!", HttpStatus.BAD_REQUEST);
    }

}
