package com.inetum.warehouse.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Builder
public class Inventory implements Serializable {

    private static final long serialVersionUID = 376436963768417453L;

    @Id
    private Long id;

    @NotNull
    private Long count;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

}
