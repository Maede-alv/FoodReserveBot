package com.maedealv.foodreservebot.controller;

import com.maedealv.foodreservebot.dto.OrderDto;
import com.maedealv.foodreservebot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> listOfOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to) {

        List<OrderDto> orders = orderService.listOfOrders(userId, from, to);
        return ResponseEntity.ok(orders);
    }
}
