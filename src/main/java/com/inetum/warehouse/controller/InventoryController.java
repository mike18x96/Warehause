package com.inetum.warehouse.controller;

import com.inetum.warehouse.dto.InventoryDto;
import com.inetum.warehouse.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addProductToInventory(@RequestParam Long code, @RequestParam  Long count) {
        if (inventoryService.checkIfItExistsCodeProduct(code)) {
            return inventoryService.addProductToInventory(code, count);
        } else {
            throw new EntityNotFoundException(String.format("Not found product with code: %s", code.toString()));
        }
    }

    @GetMapping
    public List<InventoryDto> getAll() {
        return inventoryService.findAll();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity("Incomplete object!", HttpStatus.BAD_REQUEST);
    }

}

