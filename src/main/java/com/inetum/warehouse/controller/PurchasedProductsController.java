package com.inetum.warehouse.controller;

import com.inetum.warehouse.model.Purchase;
import com.inetum.warehouse.service.PurchasedProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchasedProductsController {

    private final PurchasedProductsService purchasedProductsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase responsePurchase(@RequestBody Map<String, Long> orderedProduct) {
        return purchasedProductsService.createOrder(orderedProduct);
    }


//    @GetMapping
//    public List<PurchasedProducts> getAll() {
//        return purchasedProductsService.findAll();
//    }

}
