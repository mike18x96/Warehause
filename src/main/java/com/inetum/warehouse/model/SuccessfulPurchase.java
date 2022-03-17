package com.inetum.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SuccessfulPurchase extends AbstractPurchase {

    private boolean success;
    private Map<String, Long> purchasedProducts;

}
