package com.maedealv.foodreservebot.repository;

import com.maedealv.foodreservebot.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {

    List<FoodEntity> findAllByAvailableTrue();
}