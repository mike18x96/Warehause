package com.inetum.warehouse.service;

import com.inetum.warehouse.exception.EmptyObjectException;
import com.inetum.warehouse.model.*;
import com.inetum.warehouse.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private InventoryService inventoryService;
    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    void validateOrder_EmptyMapInParameter_throwEmptyObjectException() {
        //when
        assertThatThrownBy(() -> purchaseService
                .validateOrder(new HashMap<>()))
                .isInstanceOf(EmptyObjectException.class);
        //then
        verifyNoMoreInteractions(inventoryRepository);
    }

    @Test
    void validateOrder_codeProductNotExistInRepo_throwEntityNotFoundException() {
        //given
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(any(Inventory.class)));
        //when
        assertThatThrownBy(() -> purchaseService
                .validateOrder(new HashMap() {{
                    put("1", 50l);
                }}))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not found product in inventory with code: 1");
        //then
        verifyNoMoreInteractions(inventoryRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0, 1000, 2000})
    void validateOrder_countOfProductOutOfRange_throwEntityNotFoundException(Long countOutOfRange) {
        //given
        Product product = new Product(1l, "name", "desc");
        Inventory inventory = new Inventory(1L, 50l, product);

        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(inventory));

        //when
        assertThatThrownBy(() -> purchaseService
                .validateOrder(new HashMap() {{
                    put("1", countOutOfRange);
                }}))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Order only in the range of 0-1000");
        //then
        verifyNoMoreInteractions(inventoryRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 50, 100})
    void validateOrder_successfulPurchase_returnSuccessfulPurchaseObject(Long countOfProductInOrder) {
        //given
        Product product = new Product(1l, "name", "desc");
        Inventory inventory = new Inventory(1L, 100l, product);

        HashMap<String, Long> testMap = new HashMap<>();
        testMap.put("1", countOfProductInOrder);

        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(inventory));        //when

        AbstractPurchase result = purchaseService.validateOrder(testMap);
        assertThat(result).isEqualTo(new SuccessfulPurchase(true, testMap));
        //then
        verifyNoMoreInteractions(inventoryRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 50, 100})
    void validateOrder_unsuccessfulPurchase_returnUnsuccessfulPurchaseObject(Long countOfProductInOrder) {
        //given
        Product product = new Product(1l, "name", "desc");
        Inventory inventory = new Inventory(1L, 10l, product);

        HashMap<String, Long> testMap = new HashMap<>();
        testMap.put("1", countOfProductInOrder);

        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(inventory));        //when

        AbstractPurchase result = purchaseService.validateOrder(testMap);
        assertThat(result).isEqualTo(new UnsuccessfulPurchase(false, testMap));
        //then
        verifyNoMoreInteractions(inventoryRepository);
    }
}