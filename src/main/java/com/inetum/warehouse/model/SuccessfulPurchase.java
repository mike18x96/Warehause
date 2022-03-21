package com.inetum.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class SuccessfulPurchase extends AbstractPurchase {

    private boolean success;
    private Bill bill;
    private Map<String, Long> purchasedProducts;


}
