package com.inetum.warehouse.controller;

import com.inetum.warehouse.model.Product;
import com.inetum.warehouse.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        return productService.create(product);
    }

    @PutMapping("{id}")
    public String update(@PathVariable("id") Long id, @Valid @RequestBody Product updatedProduct) {
        try {
            return productService.update(id, updatedProduct);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(String.format("Not found product with code: %s", id.toString()));
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        try {
            productService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(String.format("Not found product with code: %s", id.toString()));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity("Values should not be empty!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidException(HttpMessageNotReadableException e) {
        return new ResponseEntity("Give the correct values!", HttpStatus.BAD_REQUEST);
    }

}
