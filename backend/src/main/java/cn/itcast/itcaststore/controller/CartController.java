package cn.itcast.itcaststore.controller;

import cn.itcast.itcaststore.dto.CartItemRequest;
import cn.itcast.itcaststore.dto.CartItemResponse;
import cn.itcast.itcaststore.security.JwtTokenProvider;
import cn.itcast.itcaststore.service.CartService;
import cn.itcast.itcaststore.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final JwtTokenProvider tokenProvider;

    public CartController(CartService cartService, JwtTokenProvider tokenProvider) {
        this.cartService = cartService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping
    public ResponseResult<List<CartItemResponse>> getCartItems(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return ResponseResult.success(cartService.getCartItems(userId));
    }

    @PostMapping
    public ResponseResult<CartItemResponse> addCartItem(
            @Valid @RequestBody CartItemRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        return ResponseResult.success(cartService.addItem(userId, request));
    }

    @PutMapping("/{productId}")
    public ResponseResult<CartItemResponse> updateCartItem(
            @PathVariable String productId,
            @Valid @RequestBody CartItemRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        return ResponseResult.success(cartService.updateQuantity(userId, productId, request.getQuantity()));
    }

    @DeleteMapping("/{productId}")
    public ResponseResult<Void> deleteCartItem(
            @PathVariable String productId,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        cartService.removeItem(userId, productId);
        return ResponseResult.success(null);
    }

    @DeleteMapping
    public ResponseResult<Void> clearCart(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        cartService.clearCart(userId);
        return ResponseResult.success(null);
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未授权");
        }
        String token = authHeader.substring(7);
        if (!tokenProvider.validateToken(token)) {
            throw new RuntimeException("Token无效或已过期");
        }
        Long userId = tokenProvider.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("无效的Token");
        }
        return userId;
    }
}
