package com.inetum.warehouse.service;

import com.inetum.warehouse.dto.InventoryDtoConverter;
import com.inetum.warehouse.dto.InventoryDto;
import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.repository.InventoryRepository;
import com.inetum.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public List<InventoryDto> findAll() {

        List<Inventory> listInventory = inventoryRepository.findAll();
        InventoryDtoConverter converter = new InventoryDtoConverter();

        return listInventory.stream()
                .map(inventory -> converter.convert(inventory))
                .collect(Collectors.toList());
    }

    public String increaseAmount(Long code, Long count) {
        if (!inventoryRepository.findInventoryByProduct(productRepository.findById(code).get()).isPresent()) {
            Inventory inventory = (Inventory.builder()
                    .id(code)
                    .count(count)
                    .product(productRepository.getById(code))
                    .build());

            inventoryRepository.save(inventory).getId();
            return appropriateReturnForIncrease(code, count, count);

        } else {
            Long previousAmountProduct = inventoryRepository.findInventoryByProduct(productRepository.findById(code).get()).get().getCount();
            Long totalCount = previousAmountProduct + count;

            Inventory inventory = Inventory.builder()
                    .id(code)
                    .count(totalCount)
                    .product(productRepository.getById(code))
                    .build();
            inventoryRepository.save(inventory);

            return appropriateReturnForIncrease(code, count, totalCount);
        }
    }

    private String appropriateReturnForIncrease(Long code, Long count, Long totalCount) {
        if (count == 1) {
            return String.format("%s piece of product have been added with the code: %s \nin total: %s", count, code, totalCount);
        } else {
            return String.format("%s pieces of product have been added with the code: %s \nin total: %s", count, code, totalCount);
        }
    }

    public String decreaseAmount(Long code, Long count) {

        Long previousAmountProduct = inventoryRepository.findInventoryByProduct(productRepository.findById(code).get()).get().getCount();
        Long totalCount = previousAmountProduct - count;

        Inventory inventory = Inventory.builder()
                .id(code)
                .count(totalCount)
                .product(productRepository.getById(code))
                .build();
        inventoryRepository.save(inventory);

        return appropriateReturnForDecrease(code, count, totalCount);

    }

    private String appropriateReturnForDecrease(Long code, Long count, Long totalCount) {
        if (count == 1) {
            return String.format("%s piece of product have been reduced with the code: %s \nin total: %s", count, code, totalCount);
        } else {
            return String.format("%s pieces of product have been reduced with the code: %s \nin total: %s", count, code, totalCount);
        }
    }

    public boolean checkIfItExistsCodeProduct(Long code) {
        return productRepository.existsById(code);
    }

}
