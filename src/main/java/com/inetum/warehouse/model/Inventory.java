package com.inetum.warehouse.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Builder
public class Inventory {

    @Id
    @NotNull
    private Long id;

    private Long count;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private Long price;

}
