//package com.inetum.warehouse.service;
//
//import com.inetum.warehouse.dto.InventoryDto;
//import com.inetum.warehouse.model.Inventory;
//import com.inetum.warehouse.model.Product;
//import com.inetum.warehouse.repository.InventoryRepository;
//import com.inetum.warehouse.repository.ProductRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class InventoryServiceTest {
//
//    @Mock
//    private InventoryRepository inventoryRepository;
//    @Mock
//    private ProductRepository productRepository;
//    @InjectMocks
//    private InventoryService inventoryService;
//
//    @Test
//    void findAll_listOfInventoryIsExist_returnListInventory() {
//        //given
//        when(inventoryRepository.findAllByCountGreaterThan(1l)).thenReturn(new ArrayList<>());
//        //when
//        inventoryService.findAll();
//        //then
//        verify(inventoryRepository, times(1)).findAllByCountGreaterThan(1L);
//        verifyNoMoreInteractions(inventoryRepository);
//    }
//
//    @Test
//    void findAll_listOfInventoryIsEmpty_returnEmptyListInventory() {
//        //given
//        when(inventoryRepository.findAllByCountGreaterThan(1l)).thenReturn(Collections.emptyList());
//        //when
//        List<InventoryDto> result = inventoryService.findAll();
//        //then
//        assertThat(result).isEmpty();
//        verify(inventoryRepository, times(1)).findAllByCountGreaterThan(1L);
//        verifyNoMoreInteractions(inventoryRepository);
//    }
//
//    @Test
//    void addProductWithAmountToInventory_correctInventory_returnEmptyListInventory() {
//        //given
//        Product product = new Product(1l, "name", "desc");
//        Inventory inventory = new Inventory(1L, 50l, product);
//
//        when(productRepository.existsById(anyLong())).thenReturn(true);
//        when(inventoryRepository.findInventoryByProduct(any(Product.class))).thenReturn(Optional.of(inventory));
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//        when(inventoryRepository.save(any(Inventory.class))).thenReturn(any());
//        //when
//        String result = inventoryService.addProductWithAmountToInventory(inventory);
//        //then
//        assertThat(result).contains("50 pieces of product have been added with the code: 1 \nin total: 100");
//        verify(inventoryRepository, times(2)).findInventoryByProduct(any());
//        verify(productRepository, times(1)).existsById(anyLong());
//        verify(productRepository, times(2)).findById(anyLong());
//        verify(productRepository, times(1)).getById(anyLong());
//        verifyNoMoreInteractions(inventoryRepository);
//        verifyNoMoreInteractions(productRepository);
//    }
//
//    @Test
//    void decreaseAmount_correctParameters_saveToRepo() {
//        //given
//        Product product = new Product(1l, "name", "desc");
//        Inventory inventory = new Inventory(1L, 50l, product);
//        when(inventoryRepository.findInventoryByProduct(any(Product.class))).thenReturn(Optional.of(inventory));
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//        //when
//        inventoryService.decreaseAmount(1L, 20l);
//        //then
//        verify(inventoryRepository, times(1)).findInventoryByProduct(any());
//        verify(inventoryRepository, times(1)).save(any());
//        verify(productRepository, times(1)).findById(anyLong());
//        verify(productRepository, times(1)).getById(anyLong());
//        verifyNoMoreInteractions(inventoryRepository);
//        verifyNoMoreInteractions(productRepository);
//    }
//}