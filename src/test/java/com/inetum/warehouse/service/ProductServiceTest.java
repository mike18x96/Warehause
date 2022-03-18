package com.inetum.warehouse.service;

import com.inetum.warehouse.model.Product;
import com.inetum.warehouse.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void findAll_listOfProductIsNotEmpty_returnListProduct() {
        //given
        Product product = new Product(1l, "name", "desc");
        when(productRepository.findAll()).thenReturn(List.of(product));
        //when
        List<Product> resultList = productService.findAll();
        //then
        assertThat(resultList).isEqualTo(List.of(product));
        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findAll_listOfProductIsEmpty_returnEmptyList() {
        //given
        when(productRepository.findAll()).thenReturn(new ArrayList<>());
        //when
        List<Product> resultList = productService.findAll();
        //then
        assertThat(resultList).isEmpty();
        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void create_givenCorrectProduct_getIdOfSavedProduct() {
        //given
        Product product = new Product(1l, "name", "desc");
        when(productRepository.save(any(Product.class))).thenReturn(product);
        //when
        Long result = productService.create(product);
        //then
        assertThat(result).isEqualTo(product.getId());
        verify(productRepository, times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void update_productWithIdIsExist_returnCorrectString() {
        //given
        Product product = new Product(1l, "name", "desc");
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        //when
        String result = productService.update(1l, product);
        //then
        assertThat(result).isEqualTo("Product with id: 1 has been updated");
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void update_productWithIdNotExist_throwException() throws EntityNotFoundException {
        //given
        when(productRepository.existsById(anyLong())).thenReturn(false);
        //when
        assertThatThrownBy(() -> productService.update(1l, any(Product.class)))
                .isInstanceOf(EntityNotFoundException.class);
        //then
        verify(productRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void delete_productWithIdIsExist_deleteProduct() {
        //given
        when(productRepository.existsById(anyLong())).thenReturn(true);
        //when
        productService.delete(anyLong());
        //then
        verify(productRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void delete_productWithIdNotExist_throwException() {
        //given
        when(productRepository.existsById(anyLong())).thenReturn(false);
        //when
        assertThatThrownBy(() -> productService.delete(anyLong()))
                .isInstanceOf(EntityNotFoundException.class);
        //then
        verify(productRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(productRepository);
    }
}