package com.inetum.warehouse.service;

import com.inetum.warehouse.dto.Inventory2InventoryDtoConverter;
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
        Inventory2InventoryDtoConverter converter = new Inventory2InventoryDtoConverter();

        return listInventory.stream()
                .map(inventory -> converter.convert(inventory))
                .collect(Collectors.toList());
    }

    public String increaseAmount(Long code, Long count) {

        if (!inventoryRepository.existsById(code)) {
            Inventory inventory = Inventory.builder()
                    .count(count)
                    .code(productRepository.getById(code))
                    .build();
            inventoryRepository.save(inventory).getId();
            return appropriateReturn(code, count, count);

        } else {
            Long previousAmountProduct = inventoryRepository.findInventoryByCode(productRepository.findById(code).get()).get().getCount();
            Long totalCount = previousAmountProduct + count;

            Inventory inventory = Inventory.builder()
                    .id(code)
                    .count(totalCount)
                    .code(productRepository.getById(code))
                    .build();
            inventoryRepository.save(inventory);

            return appropriateReturn(code, count, totalCount);
        }
    }

    private String appropriateReturn(Long code, Long count, Long totalCount) {
        if (count == 1) {
            return String.format("%s piece of product have been added with the code: %s \nin total: %s", count, code, totalCount);
        } else {
            return String.format("%s pieces of product have been added with the code: %s \nin total: %s", count, code, totalCount);
        }
    }

    public boolean checkIfItExistsCodeProduct(Long code) {
        return productRepository.existsById(code);
    }

}
