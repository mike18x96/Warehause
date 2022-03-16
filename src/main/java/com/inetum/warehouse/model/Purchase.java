package com.inetum.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
public class Purchase {

    @Id
    private Long id;
    private boolean success;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "purchase_mapping", joinColumns = @JoinColumn(name = "purchase_id"))
    @MapKeyColumn(name = "code")
    @Column(name = "count")
    private Map<String, Long> purchasedProducts;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "purchase_missing_mapping", joinColumns = @JoinColumn(name = "purchase_id"))
    @MapKeyColumn(name = "code")
    @Column(name = "count")
    private Map<String, Long> missingProducts;

}
