package com.maedealv.foodreservebot.dto;

import com.maedealv.foodreservebot.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(
        Long id,
        String userTelegramId,
        String username,
        String foodName,
        BigDecimal foodPrice,
        String status,
        LocalDateTime createdAt
) {
    public static OrderDto from(OrderEntity entity) {
        if (entity == null) return null;
        var user = entity.getUser();
        var food = entity.getFood();

        return new OrderDto(
                entity.getId(),
                user.getTelegramUserId().toString(),
                user.getUsername(),
                food.getName(),
                food.getPrice(),
                entity.getStatus().name(),
                entity.getCreatedAt()
        );
    }
}