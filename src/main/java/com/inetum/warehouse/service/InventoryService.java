package com.inetum.warehouse.service;

import com.inetum.warehouse.dto.InventoryDtoConverter;
import com.inetum.warehouse.dto.InventoryDto;
import com.inetum.warehouse.exception.WrongRangeException;
import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.repository.InventoryRepository;
import com.inetum.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public List<InventoryDto> findAll() {

        List<Inventory> listInventory = inventoryRepository.findAllByCountGreaterThan(1l);
        InventoryDtoConverter converter = new InventoryDtoConverter();

        return listInventory.stream()
                .map(inventory -> converter.convert(inventory))
                .collect(Collectors.toList());
    }

    public String increaseAmount(Inventory inventory) {

        validateProductToInventory(inventory);

        if (!inventoryRepository.findInventoryByProduct(productRepository.findById(inventory.getId()).get()).isPresent()) {
            Inventory newInventory = (Inventory.builder()
                    .id(inventory.getId())
                    .count(inventory.getCount())
                    .product(productRepository.getById(inventory.getId()))
                    .build());

            inventoryRepository.save(newInventory).getId();
            return appropriateReturnForIncrease(inventory.getId(), inventory.getCount(), inventory.getCount());

        } else {
            Long previousAmountProduct = inventoryRepository.findInventoryByProduct(productRepository.findById(inventory.getId()).get()).get().getCount();
            Long totalCount = previousAmountProduct + inventory.getCount();

            Inventory newInventory = Inventory.builder()
                    .id(inventory.getId())
                    .count(totalCount)
                    .product(productRepository.getById(inventory.getId()))
                    .build();
            inventoryRepository.save(newInventory);

            return appropriateReturnForIncrease(inventory.getId(), inventory.getCount(), totalCount);
        }
    }

    public void decreaseAmount(Long code, Long count) {

        Long previousAmountProduct = inventoryRepository
                .findInventoryByProduct(productRepository.findById(code).get()).get().getCount();
        Long totalCount = previousAmountProduct - count;
        Inventory inventory = Inventory.builder()
                .id(code)
                .count(totalCount)
                .product(productRepository.getById(code))
                .build();
        inventoryRepository.save(inventory);

    }

    private void validateProductToInventory(Inventory inventory) {

        if(!isCodeProductInRepository(inventory.getId())){
            throw new EntityNotFoundException(String.format("Not found product with code: %s", inventory.getId()));
        }

        if (!isCountProductInRange(inventory.getCount())) {
            throw new WrongRangeException("Count must be between 1 and 999");
        }

    }

    private boolean isCodeProductInRepository(Long code) {
        return productRepository.existsById(code);
    }

    private String appropriateReturnForIncrease(Long code, Long count, Long totalCount) {
        if (count == 1) {
            return String.format("%s piece of product have been added with the code: %s \nin total: %s", count, code, totalCount);
        } else {
            return String.format("%s pieces of product have been added with the code: %s \nin total: %s", count, code, totalCount);
        }
    }

    private boolean isCountProductInRange(Long count) {
        if (count > 0 && count < 1000) {
            return true;
        } else {
            return false;
        }
    }

}
