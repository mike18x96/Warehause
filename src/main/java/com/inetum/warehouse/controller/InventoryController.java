package com.inetum.warehouse.controller;

import com.inetum.warehouse.dto.InventoryDto;
import com.inetum.warehouse.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String increaseAmount(@RequestParam Long code, @Min(1) @Max(999) @RequestParam  Long count) {
        if (inventoryService.checkIfItExistsCodeProduct(code)) {
            return inventoryService.increaseAmount(code, count);
        } else {
            throw new EntityNotFoundException(code.toString());
        }
    }

    @GetMapping
    public List<InventoryDto> getAll() {
        return inventoryService.findAll();
    }

}

