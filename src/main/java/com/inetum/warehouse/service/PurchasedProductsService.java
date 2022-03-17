package com.inetum.warehouse.service;

import com.inetum.warehouse.exception.EmptyOrderException;
import com.inetum.warehouse.model.AbstractPurchase;
import com.inetum.warehouse.model.SuccessfulPurchase;
import com.inetum.warehouse.repository.InventoryRepository;
import com.inetum.warehouse.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Service
@AllArgsConstructor
public class PurchasedProductsService {

    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final ProductRepository productRepository;

    private Map<String, Long> purchasedProducts;
    private Map<String, Long> missingProducts;

    public AbstractPurchase createOrder(Map<String, Long> orderedProduct) {

        isNotEmptyOrder(orderedProduct.size());

        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            isPresentInInventory(key);
            checkRangeAmountProductInOrder(value);

            Long totalAmountInInventory = inventoryRepository
                    .findInventoryByProduct(productRepository.findById(Long.valueOf(key)).get()).get().getCount();
            if (checkEnoughInInventory(totalAmountInInventory, value, key)) {
                inventoryService.decreaseAmount(Long.valueOf(key), value);
                purchasedProducts.put(key, value);
            } else {
                missingProducts.put(key, value);
            }
        }
        return new SuccessfulPurchase(true, purchasedProducts);
    }

    public Map returnMissingProduct() {
        return missingProducts;
    }

    private boolean isNotEmptyOrder(int count) {
        if (count > 0) {
            return true;
        } else {
            throw new EmptyOrderException();
        }
    }

    private boolean isPresentInInventory(String key) {
        if (inventoryRepository.findById(Long.valueOf(key)).isPresent()) {
            return true;
        } else {
            throw new EntityNotFoundException(String.format("Not found product in inventory with code: %s", key));
        }
    }

    private boolean checkRangeAmountProductInOrder(Long count) {
        if (count > 0 && count < 1000) {
            return true;
        } else {
            throw new EntityNotFoundException("Order only in the range of 0-1000" + "\n");
        }
    }

    private boolean checkEnoughInInventory(Long totalAmountInInventory, Long requiredCount, String code) {
        if (totalAmountInInventory >= requiredCount) {
            return true;
        } else {
            return false;
        }
    }
}
