package com.inetum.warehouse.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Builder
public class Inventory {

    @Id
    private Long id;

    private Long count;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
