package com.inetum.warehouse.service;

import com.google.gson.Gson;
import com.inetum.warehouse.model.Purchase;
import com.inetum.warehouse.repository.InventoryRepository;
import com.inetum.warehouse.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional
public class PurchasedProductsService {

    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final ProductRepository productRepository;
    private final Purchase purchase;

    private Map<String, Long> purchasedProducts;
    private Map<String, Long> missingProducts;

//    public String responsePurchase(Map<String,Long> orderedProduct){
//
//        createOrder(orderedProduct);
//
//        if(orderedProduct.)
//        return
//    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Purchase createOrder(Map<String, Long> orderedProduct) {

        checkMinOneProduct(orderedProduct.size());


        for (Map.Entry<String, Long> entry : orderedProduct.entrySet()) {
            String k = entry.getKey();
            Long v = entry.getValue();

            checkOrderRange(v);
            checkIsPresent(k, orderedProduct);
            Long totalAmountInInventory = inventoryRepository
                    .findInventoryByProduct(productRepository.findById(Long.valueOf(k)).get()).get().getCount();


            if (checkEnoughInInventory(totalAmountInInventory, v, k)) {
                inventoryService.decreaseAmount(Long.valueOf(k), v);
                purchasedProducts.put(k, v);
                purchase.setPurchasedProducts(purchasedProducts);

            } else {
                missingProducts.put(k, v);
                purchase.setMissingProducts(missingProducts);
            }

        }

        if (missingProducts.isEmpty()) {
            return purchase;
        } else {
            return purchase;
        }

    }

    private boolean checkMinOneProduct(int count) {
        if (count > 0) {
            return true;
        } else {
            throw new EntityNotFoundException("Min one product in order!");
        }
    }

    private boolean checkIsPresent(String firstKey, Map<String, Long> orderedProduct) {
        if (orderedProduct.containsKey(firstKey)) {
            return true;
        } else {
            throw new EntityNotFoundException("Not found product with CODE: " + firstKey + "\n");
        }
    }

    private boolean checkOrderRange(Long count) {
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

//    public String convertWithGuava(Map<String, Long> map) {
//        return Joiner.on(",").withKeyValueSeparator("=").join(map);
//    }

//    private String appropriateReturn(String code, Long count, Long totalCount) {
//        if (count == 0) {
//            return String.format("We don't have the product: %s" + "\n", code);
//        } else if (count == 1) {
//            return String.format("Inventory has %s piece of %s product" + "\n", totalCount, code);
//        } else if (count > 1) {
//            return String.format("Inventory has %s pieces of %s product" + "\n", totalCount, code);
//        }
//        return null;
//    }


}
