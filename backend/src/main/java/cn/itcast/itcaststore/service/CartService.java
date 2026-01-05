package cn.itcast.itcaststore.service;

import cn.itcast.itcaststore.domain.CartItem;
import cn.itcast.itcaststore.domain.Product;
import cn.itcast.itcaststore.dto.CartItemRequest;
import cn.itcast.itcaststore.dto.CartItemResponse;
import cn.itcast.itcaststore.repository.CartItemRepository;
import cn.itcast.itcaststore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long userId) {
        if (userId == null) {
            throw new RuntimeException("未授权");
        }
        return cartItemRepository.findWithProductByUserId(userId).stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemResponse addItem(Long userId, CartItemRequest request) {
        if (userId == null) {
            throw new RuntimeException("未授权");
        }
        if (request == null) {
            throw new RuntimeException("请求不能为空");
        }
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        int addQuantity = Optional.ofNullable(request.getQuantity()).orElse(0);
        if (addQuantity <= 0) {
            throw new RuntimeException("数量必须大于0");
        }

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, product.getId())
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setUserId(userId);
                    item.setProduct(product);
                    item.setQuantity(0);
                    return item;
                });

        int newQuantity = cartItem.getQuantity() + addQuantity;
        validateStock(product, newQuantity);

        cartItem.setQuantity(newQuantity);
        CartItem saved = cartItemRepository.save(cartItem);
        saved.setProduct(product);
        return CartItemResponse.from(saved);
    }

    @Transactional
    public CartItemResponse updateQuantity(Long userId, String productId, Integer quantity) {
        if (userId == null) {
            throw new RuntimeException("未授权");
        }
        if (productId == null || productId.isBlank()) {
            throw new RuntimeException("商品ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("数量必须大于0");
        }

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("购物车中没有该商品"));

        Product product = cartItem.getProduct();
        if (product == null) {
            product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("商品不存在"));
            cartItem.setProduct(product);
        }

        validateStock(product, quantity);

        cartItem.setQuantity(quantity);
        CartItem saved = cartItemRepository.save(cartItem);
        saved.setProduct(product);
        return CartItemResponse.from(saved);
    }

    @Transactional
    public void removeItem(Long userId, String productId) {
        if (userId == null) {
            throw new RuntimeException("未授权");
        }
        if (productId == null || productId.isBlank()) {
            throw new RuntimeException("商品ID不能为空");
        }

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("购物车中没有该商品"));
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {
        if (userId == null) {
            throw new RuntimeException("未授权");
        }
        cartItemRepository.deleteByUserId(userId);
    }

    private void validateStock(Product product, int quantity) {
        if (product.getPnum() != null && product.getPnum() < quantity) {
            throw new RuntimeException("库存不足: " + product.getName());
        }
    }
}
