package com.maedealv.foodreservebot.service;

import com.maedealv.foodreservebot.entity.UserEntity;
import com.maedealv.foodreservebot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserEntity getOrCreateByTelegramUserId(Long telegramUserId) {
        return userRepository.findByTelegramUserId(telegramUserId)
                .orElseGet(() -> {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setTelegramUserId(telegramUserId);
                    return userRepository.save(userEntity);
                });
    }
}

