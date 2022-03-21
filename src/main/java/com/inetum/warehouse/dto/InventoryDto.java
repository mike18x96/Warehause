package com.inetum.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class InventoryDto {

    private Long code;
    private Long count;
    private Long price;

}
