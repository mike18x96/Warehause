package com.inetum.warehouse.service;

import com.inetum.warehouse.model.Product;
import com.inetum.warehouse.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Long save(Product product) {
        return productRepository.save(product).getId();

    }

    public String update(Long id, Product newProduct) {
        if (productRepository.existsById(id)) {
            newProduct.setId(id);
            productRepository.save(newProduct);
            return "Product with id: " + id + " has been updated";
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
