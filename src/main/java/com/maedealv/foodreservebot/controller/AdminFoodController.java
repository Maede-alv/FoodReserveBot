package com.maedealv.foodreservebot.controller;

import com.maedealv.foodreservebot.dto.FoodDto;
import com.maedealv.foodreservebot.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/foods")
@RequiredArgsConstructor
public class AdminFoodController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodDto> createFood(@Valid @RequestBody FoodDto foodDto) {
        FoodDto created = foodService.createFood(foodDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<FoodDto> getAllFoods() {
        return foodService.getAllFoods();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDto> getFood(@PathVariable Long id) {
        FoodDto food = foodService.findById(id);
        return food != null ? ResponseEntity.ok(food) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FoodDto> updateFood(@PathVariable Long id, @RequestBody FoodDto foodDto) {
        FoodDto updated = foodService.updateFood(id, foodDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}