package cn.itcast.itcaststore.controller;

import cn.itcast.itcaststore.domain.Order;
import cn.itcast.itcaststore.domain.OrderItem;
import cn.itcast.itcaststore.domain.OrderItemId;
import cn.itcast.itcaststore.dto.CreateOrderRequest;
import cn.itcast.itcaststore.repository.OrderRepository;
import cn.itcast.itcaststore.repository.OrderItemRepository;
import cn.itcast.itcaststore.security.JwtTokenProvider;
import cn.itcast.itcaststore.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final JwtTokenProvider tokenProvider;

    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, JwtTokenProvider tokenProvider) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
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
    @Transactional
    public ResponseResult<Order> createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest httpRequest) {
        // #region agent log
        try {
            String logEntry = String.format("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"OrderController.java:73\",\"message\":\"createOrder entry\",\"data\":{\"orderItemsCount\":%d},\"timestamp\":%d}\n",
                request.getOrderItems() != null ? request.getOrderItems().size() : 0, System.currentTimeMillis());
            System.out.print(logEntry);
            String logPath = "/app/.cursor/debug.log";
            Files.createDirectories(Paths.get("/app/.cursor"));
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Log write error: " + e.getMessage());
            e.printStackTrace();
        }
        // #endregion
        
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未授权");
        }
        String token = authHeader.substring(7);
        Long userId = tokenProvider.getUserIdFromToken(token);
        
        // 生成订单ID
        String orderId = UUID.randomUUID().toString();
        
        // #region agent log
        try {
            String logEntry = String.format("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"B\",\"location\":\"OrderController.java:95\",\"message\":\"orderId generated\",\"data\":{\"orderId\":\"%s\"},\"timestamp\":%d}\n",
                orderId, System.currentTimeMillis());
            System.out.print(logEntry);
            String logPath = "/app/.cursor/debug.log";
            Files.createDirectories(Paths.get("/app/.cursor"));
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Log write error: " + e.getMessage());
        }
        // #endregion
        
        // 创建订单对象
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setPaystate(0);
        order.setMoney(request.getMoney());
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        
        // 先保存订单，确保订单ID存在（使用 flush 确保立即写入数据库）
        Order savedOrder = orderRepository.saveAndFlush(order);
        
        // #region agent log
        try {
            String logEntry = String.format("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"C\",\"location\":\"OrderController.java:117\",\"message\":\"order saved\",\"data\":{\"savedOrderId\":\"%s\",\"savedOrderIdFromObject\":\"%s\"},\"timestamp\":%d}\n",
                savedOrder.getId(), order.getId(), System.currentTimeMillis());
            System.out.print(logEntry);
            String logPath = "/app/.cursor/debug.log";
            Files.createDirectories(Paths.get("/app/.cursor"));
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Log write error: " + e.getMessage());
        }
        // #endregion
        
        // 创建并保存订单项
        if (request.getOrderItems() != null && !request.getOrderItems().isEmpty()) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (CreateOrderRequest.OrderItemRequest itemRequest : request.getOrderItems()) {
                // 创建 OrderItemId
                OrderItemId itemId = new OrderItemId();
                itemId.setOrderId(orderId);
                itemId.setProductId(itemRequest.getProductId());
                
                // #region agent log
                try {
                    String logEntry = String.format("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"OrderController.java:135\",\"message\":\"OrderItemId created\",\"data\":{\"orderId\":\"%s\",\"productId\":\"%s\"},\"timestamp\":%d}\n",
                        itemId.getOrderId(), itemId.getProductId(), System.currentTimeMillis());
                    System.out.print(logEntry);
                    String logPath = "/app/.cursor/debug.log";
                    Files.createDirectories(Paths.get("/app/.cursor"));
                    Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (Exception e) {
                    System.err.println("Log write error: " + e.getMessage());
                }
                // #endregion
                
                // 创建 OrderItem
                OrderItem orderItem = new OrderItem();
                orderItem.setId(itemId);
                orderItem.setBuynum(itemRequest.getBuynum());
                orderItem.setOrder(savedOrder);
                
                // #region agent log
                try {
                    String logEntry = String.format("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\",\"location\":\"OrderController.java:152\",\"message\":\"OrderItem before save\",\"data\":{\"itemIdOrderId\":\"%s\",\"itemIdProductId\":\"%s\",\"buynum\":%d},\"timestamp\":%d}\n",
                        orderItem.getId() != null ? orderItem.getId().getOrderId() : "null",
                        orderItem.getId() != null ? orderItem.getId().getProductId() : "null",
                        orderItem.getBuynum(), System.currentTimeMillis());
                    System.out.print(logEntry);
                    String logPath = "/app/.cursor/debug.log";
                    Files.createDirectories(Paths.get("/app/.cursor"));
                    Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (Exception e) {
                    System.err.println("Log write error: " + e.getMessage());
                }
                // #endregion
                
                // 保存订单项
                orderItemRepository.saveAndFlush(orderItem);
                
                // #region agent log
                try {
                    String logEntry = String.format("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"E\",\"location\":\"OrderController.java:164\",\"message\":\"OrderItem after save\",\"data\":{\"itemIdOrderId\":\"%s\",\"itemIdProductId\":\"%s\"},\"timestamp\":%d}\n",
                        orderItem.getId() != null ? orderItem.getId().getOrderId() : "null",
                        orderItem.getId() != null ? orderItem.getId().getProductId() : "null", System.currentTimeMillis());
                    System.out.print(logEntry);
                    String logPath = "/app/.cursor/debug.log";
                    Files.createDirectories(Paths.get("/app/.cursor"));
                    Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (Exception e) {
                    System.err.println("Log write error: " + e.getMessage());
                }
                // #endregion
                
                orderItems.add(orderItem);
            }
            savedOrder.setOrderItems(orderItems);
        }
        
        // #region agent log
        try {
            String logEntry = String.format("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"F\",\"location\":\"OrderController.java:181\",\"message\":\"createOrder exit\",\"data\":{\"orderId\":\"%s\"},\"timestamp\":%d}\n",
                savedOrder.getId(), System.currentTimeMillis());
            System.out.print(logEntry);
            String logPath = "/app/.cursor/debug.log";
            Files.createDirectories(Paths.get("/app/.cursor"));
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Log write error: " + e.getMessage());
        }
        // #endregion
        
        return ResponseResult.success(savedOrder);
    }
}
