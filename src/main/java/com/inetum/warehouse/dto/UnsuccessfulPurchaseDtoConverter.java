package com.inetum.warehouse.dto;

import com.inetum.warehouse.model.Purchase;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UnsuccessfulPurchaseDtoConverter implements Converter<Purchase, UnsuccessfulPurchaseDto> {

    @Override
    public UnsuccessfulPurchaseDto convert(Purchase purchase) {
        return new UnsuccessfulPurchaseDto(false, purchase.getMissingProducts());
    }
}
