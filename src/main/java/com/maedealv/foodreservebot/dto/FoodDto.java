package com.maedealv.foodreservebot.dto;

import com.maedealv.foodreservebot.entity.FoodEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Optional;

public record FoodDto(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank
        String name,

        String description,

        @NotNull
        @Positive
        BigDecimal price,

        Boolean available

) {

    public static FoodDto from(FoodEntity entity) {
        if (entity == null) return null;
        return new FoodDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isAvailable()
        );
    }

    public FoodEntity toEntity() {
        FoodEntity entity = new FoodEntity();
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price);
        entity.setAvailable(this.available);
        return entity;
    }

    public void updateEntity(FoodEntity entity) {
        Optional.ofNullable(this.name).ifPresent(entity::setName);
        Optional.ofNullable(this.description).ifPresent(entity::setDescription);
        Optional.ofNullable(this.price).ifPresent(entity::setPrice);
        entity.setAvailable(this.available);
    }
}