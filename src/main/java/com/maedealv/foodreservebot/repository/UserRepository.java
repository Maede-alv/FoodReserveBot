package com.maedealv.foodreservebot.repository;

import com.maedealv.foodreservebot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByTelegramUserId(Long telegramUserId);
}

