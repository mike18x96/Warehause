package com.inetum.warehouse.controller;

import com.inetum.warehouse.model.Product;
import com.inetum.warehouse.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid Product product) {
        return productService.save(product);
    }

    @PutMapping("{id}")
    public String update(@PathVariable("id") Long id, @Valid @RequestBody Product updatedProduct) {
        try {
            return productService.update(id, updatedProduct);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        try {
            productService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

}
