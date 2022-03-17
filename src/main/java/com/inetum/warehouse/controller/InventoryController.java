package com.inetum.warehouse.controller;

import com.inetum.warehouse.dto.InventoryDto;
import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addProductToInventory(@RequestBody Inventory inventory) {
        return inventoryService.increaseAmount(inventory);
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

