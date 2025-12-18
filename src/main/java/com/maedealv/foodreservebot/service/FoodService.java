package com.maedealv.foodreservebot.service;

import com.maedealv.foodreservebot.dto.FoodDto;
import com.maedealv.foodreservebot.entity.FoodEntity;
import com.maedealv.foodreservebot.repository.FoodRepository;
import com.maedealv.foodreservebot.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;

    public FoodService(FoodRepository foodRepository, OrderRepository orderRepository) {
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<FoodDto> getAllFoods() {
        return foodRepository.findAll()
                .stream()
                .map(FoodDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public FoodDto findById(Long id) {
        return foodRepository.findById(id)
                .map(FoodDto::from)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<FoodDto> getAvailableFoods() {
        return foodRepository.findAllByAvailableTrue()
                .stream()
                .map(FoodDto::from)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public FoodDto createFood(FoodDto foodDto) {
        FoodEntity entity = foodDto.toEntity();
        FoodEntity saved = foodRepository.save(entity);
        return FoodDto.from(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public FoodDto updateFood(Long id, FoodDto foodDto) {
        return foodRepository.findById(id)
                .map(existing -> {
                    foodDto.updateEntity(existing);
                    FoodEntity updated = foodRepository.save(existing);
                    return FoodDto.from(updated);
                })
                .orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteFood(Long id) {
        if (foodRepository.existsById(id) && !orderRepository.existsByFoodId(id)) {
            foodRepository.deleteById(id);
        }
    }
}