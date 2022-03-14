package com.inetum.warehouse.dto;

import com.inetum.warehouse.model.Inventory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Inventory2InventoryDtoConverter implements Converter<Inventory, InventoryDto> {
    @Override
    public InventoryDto convert(Inventory inventory) {

        return new InventoryDto(inventory.getCode().getId(), inventory.getCount());
    }
}
