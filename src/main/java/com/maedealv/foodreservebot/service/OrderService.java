package com.maedealv.foodreservebot.service;

import com.maedealv.foodreservebot.dto.OrderDto;
import com.maedealv.foodreservebot.entity.FoodEntity;
import com.maedealv.foodreservebot.entity.OrderEntity;
import com.maedealv.foodreservebot.entity.UserEntity;
import com.maedealv.foodreservebot.repository.FoodRepository;
import com.maedealv.foodreservebot.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final FoodRepository foodRepository;

    public OrderService(OrderRepository orderRepository, UserService userService, FoodRepository foodRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.foodRepository = foodRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Long telegramUserId, Long foodId) {
        UserEntity userEntity = userService.getOrCreateByTelegramUserId(telegramUserId);
        FoodEntity foodEntity = foodRepository.getReferenceById(foodId);

        if (!foodEntity.isAvailable()) {
            throw new IllegalStateException("Food not available");
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(userEntity);
        orderEntity.setFood(foodEntity);

        orderRepository.save(orderEntity);
    }

    public List<OrderDto> listOfOrders(Long userId, LocalDateTime from, LocalDateTime to) {
        List<OrderEntity> orders = orderRepository.filterOrders(userId, from, to);

        return orders.stream().map(OrderDto::from).toList();
    }
}

