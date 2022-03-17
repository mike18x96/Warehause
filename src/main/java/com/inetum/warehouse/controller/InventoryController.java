package com.inetum.warehouse.controller;

import com.inetum.warehouse.dto.InventoryDto;
import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidException(HttpMessageNotReadableException e) {
        return new ResponseEntity("Give the correct values!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleValidException(NumberFormatException e) {
        return new ResponseEntity("Give the correct values!", HttpStatus.BAD_REQUEST);
    }
}

