package com.inetum.warehouse.service;

import com.inetum.warehouse.exception.EmptyOrderException;
import com.inetum.warehouse.model.AbstractPurchase;
import com.inetum.warehouse.model.PurchaseProcessingResult;
import com.inetum.warehouse.model.SuccessfulPurchase;
import com.inetum.warehouse.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Service
@AllArgsConstructor
public class PurchasedProductsService {

    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;

    public AbstractPurchase validateOrder(Map<String, Long> orderedProduct) {

        PurchaseProcessingResult purchaseProcessingResult = new PurchaseProcessingResult();

        isNotEmpty(orderedProduct.size());
        validateCorrectCode(orderedProduct);
        validateRangeOfCountOfProduct(orderedProduct);

        createPurchaseProcessingResult(orderedProduct, purchaseProcessingResult);

        if (purchaseProcessingResult.getMissingProducts().isEmpty()) {
            updateAmountInInventoryAfterPurchase(orderedProduct);
            return new SuccessfulPurchase(true, purchaseProcessingResult.getPurchasedProducts());
        } else {
            return new SuccessfulPurchase(false, purchaseProcessingResult.getMissingProducts());
        }
    }

    private void validateCorrectCode(Map<String, Long> orderedProduct) {
        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            String key = entry.getKey();

            if (!inventoryRepository.findById(Long.valueOf(key)).isPresent()) {
                throw new EntityNotFoundException(String.format("Not found product in inventory with code: %s", key));
            }
        }
    }

    private boolean isNotEmpty(int count) {
        if (count > 0) {
            return true;
        } else {
            throw new EmptyOrderException("The order can not be empty!");
        }
    }

    private void validateRangeOfCountOfProduct(Map<String, Long> orderedProduct) {
        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            Long value = entry.getValue();

            if (!(value > 0 && value < 1000)) {
                throw new EntityNotFoundException("Order only in the range of 0-1000" + "\n");
            }
        }
    }

    private void createPurchaseProcessingResult(Map<String, Long> orderedProduct, PurchaseProcessingResult
            purchaseProcessingResult) {
        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if (isEnoughInInventory(Long.valueOf(key), value)) {
                purchaseProcessingResult.addToPurchasedProduct(key, value);
            } else {
                purchaseProcessingResult.addToMissingProduct(key, value);
            }
        }
    }

    private boolean isEnoughInInventory(Long code, Long requestAmount) {
        if (inventoryRepository.findById(code).get().getCount() >= requestAmount) {
            return true;
        } else {
            return false;
        }
    }

    private void updateAmountInInventoryAfterPurchase(Map<String, Long> orderedProduct) {
        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            inventoryService.decreaseAmount(Long.valueOf(key), value);
        }
    }
}
