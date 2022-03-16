package com.inetum.warehouse.repository;

import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findInventoryByProduct(Product product);

    @Query("SELECT i FROM Inventory i WHERE i.product.id = :id")
    Optional<Inventory> findInventoryByProduct(@Param("id") Long id);

}
