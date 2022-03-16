package com.inetum.warehouse.dto;

import com.inetum.warehouse.model.Purchase;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SuccessfulPurchaseDtoConverter implements Converter<Purchase, SuccessfulPurchaseDto> {

    @Override
    public SuccessfulPurchaseDto convert(Purchase purchase) {
        return new SuccessfulPurchaseDto(true, purchase.getPurchasedProducts());
    }
}
