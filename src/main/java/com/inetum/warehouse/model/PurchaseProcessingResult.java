package com.inetum.warehouse.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PurchaseProcessingResult {

    private Map<String, Long> purchasedProducts = new HashMap<>();
    private Map<String, Long> missingProducts = new HashMap<>();

    public void addToPurchasedProduct(String codeProduct, Long amountOfProducts){
        purchasedProducts.put(codeProduct, amountOfProducts);
    }

    public void addToMissingProduct(String codeProduct, Long amountOfProducts){
        missingProducts.put(codeProduct, amountOfProducts);
    }

}
