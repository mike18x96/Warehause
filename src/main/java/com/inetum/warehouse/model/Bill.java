package com.inetum.warehouse.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class Bill {

    private BigDecimal totalBill;
    private BigDecimal discountPercentage;
    private BigDecimal totalBillAfterDiscount;


}
