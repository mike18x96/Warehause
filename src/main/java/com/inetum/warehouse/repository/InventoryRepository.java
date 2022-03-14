package com.inetum.warehouse.repository;

import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findInventoryByCode(Product code);
}
