package com.inetum.warehouse.service;

import com.inetum.warehouse.exception.EmptyObjectException;
import com.inetum.warehouse.model.*;
import com.inetum.warehouse.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Map;

@Service
@AllArgsConstructor
public class PurchaseService {

    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;

    public AbstractPurchase validateOrder(Map<String, Long> orderedProduct) {

        PurchaseProcessingResult purchaseProcessingResult = new PurchaseProcessingResult();
        Bill totalBill;

        isNotEmpty(orderedProduct.size());
        checkCodeProductIfExistInRepo(orderedProduct);
        validateRangeOfCountOfProduct(orderedProduct);

        createPurchaseProcessingResult(orderedProduct, purchaseProcessingResult);

        if (purchaseProcessingResult.getMissingProducts().isEmpty()) {
            updateAmountInInventoryAfterPurchase(orderedProduct);
            totalBill = createBill(orderedProduct);
            return new SuccessfulPurchase(true, totalBill, purchaseProcessingResult.getPurchasedProducts());
        } else {
            return new UnsuccessfulPurchase(false, purchaseProcessingResult.getMissingProducts());
        }
    }

    private void checkCodeProductIfExistInRepo(Map<String, Long> orderedProduct) {
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
            throw new EmptyObjectException();
        }
    }

    private void validateRangeOfCountOfProduct(Map<String, Long> orderedProduct) {
        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            Long value = entry.getValue();

            if (!(value > 0 && value < 1000)) {
                throw new EntityNotFoundException("Order only in the range of 0-1000");
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

    private Bill createBill(Map<String, Long> orderedProduct) {
        BigDecimal purchaseBill = new BigDecimal(0L);

        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            String code = entry.getKey();
            Long amount = entry.getValue();

            purchaseBill = purchaseBill.add(BigDecimal
                    .valueOf(amount * inventoryRepository.findById(Long.valueOf(code)).get().getPrice()));
        }

        if (purchaseBill.compareTo(BigDecimal.valueOf(200l)) <= 0) {
            return Bill.builder()
                    .totalBill(purchaseBill)
                    .discountPercentage(BigDecimal.ZERO)
                    .totalBillAfterDiscount(purchaseBill)
                    .build();
        } else {
            return Bill.builder()
                    .totalBill(purchaseBill)
                    .discountPercentage(BigDecimal.valueOf(0.05))
                    .totalBillAfterDiscount(purchaseBill.multiply(BigDecimal.valueOf(1 - 0.05)))
                    .build();
        }
    }
}
