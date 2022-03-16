package com.inetum.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Data
@Getter
public class UnsuccessfulPurchaseDto {

    private boolean success;
    private Map<String, Long> missingProducts;

}
