package com.maedealv.foodreservebot.repository;

import com.maedealv.foodreservebot.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
    SELECT o FROM OrderEntity o
    WHERE (:userId IS NULL OR o.user.id = :userId)
    AND (:from IS NULL OR o.createdAt >= :from)
    AND (:to IS NULL OR o.createdAt <= :to)
    ORDER BY o.createdAt DESC
""")
    List<OrderEntity> filterOrders(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    Boolean existsByFoodId(Long foodId);
}

