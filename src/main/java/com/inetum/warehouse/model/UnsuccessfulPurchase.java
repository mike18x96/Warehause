package com.inetum.warehouse.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class UnsuccessfulPurchase extends AbstractPurchase {

    private Map<String, Long> missingProducts;

    public UnsuccessfulPurchase(boolean success, Map<String, Long> missingProducts) {
        super(success);
        this.missingProducts = missingProducts;
    }
}
