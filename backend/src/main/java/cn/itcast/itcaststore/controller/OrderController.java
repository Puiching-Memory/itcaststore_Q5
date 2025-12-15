package cn.itcast.itcaststore.controller;

import cn.itcast.itcaststore.domain.Order;
import cn.itcast.itcaststore.repository.OrderRepository;
import cn.itcast.itcaststore.security.JwtTokenProvider;
import cn.itcast.itcaststore.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final JwtTokenProvider tokenProvider;

    public OrderController(OrderRepository orderRepository, JwtTokenProvider tokenProvider) {
        this.orderRepository = orderRepository;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping
    public ResponseResult<Page<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未授权");
        }
        String token = authHeader.substring(7);
        Long userId = tokenProvider.getUserIdFromToken(token);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return ResponseResult.success(orders);
    }

    @GetMapping("/list")
    public ResponseResult<List<Order>> getOrderList(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未授权");
        }
        String token = authHeader.substring(7);
        Long userId = tokenProvider.getUserIdFromToken(token);
        
        List<Order> orders = orderRepository.findByUserIdOrderByOrdertimeDesc(userId);
        return ResponseResult.success(orders);
    }

    @PostMapping
    public ResponseResult<Order> createOrder(@RequestBody Order order, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未授权");
        }
        String token = authHeader.substring(7);
        Long userId = tokenProvider.getUserIdFromToken(token);
        
        order.setId(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setPaystate(0);
        
        Order savedOrder = orderRepository.save(order);
        return ResponseResult.success(savedOrder);
    }
}
