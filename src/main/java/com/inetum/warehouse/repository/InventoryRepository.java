package com.inetum.warehouse.repository;

import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findInventoryByProduct(Product product);
    List<Inventory> findAllByCountGreaterThan(Long l);

}
