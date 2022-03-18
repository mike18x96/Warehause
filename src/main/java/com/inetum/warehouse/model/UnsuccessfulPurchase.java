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
public class UnsuccessfulPurchase extends AbstractPurchase {

    private boolean success;
    private Map<String, Long> missingProducts;

}
