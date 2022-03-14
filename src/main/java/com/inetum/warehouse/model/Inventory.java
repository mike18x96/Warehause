package com.inetum.warehouse.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long count;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private Product code;

}
