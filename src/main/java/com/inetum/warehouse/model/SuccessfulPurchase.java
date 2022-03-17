package com.inetum.warehouse.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class SuccessfulPurchase extends AbstractPurchase {

    private Map<String, Long> purchasedProducts;

    public SuccessfulPurchase(boolean success, Map<String, Long> purchasedProducts) {
        super(success);
        this.purchasedProducts = purchasedProducts;
    }
}
