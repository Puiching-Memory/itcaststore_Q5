package cn.itcast.itcaststore.dto;

import cn.itcast.itcaststore.domain.CartItem;
import cn.itcast.itcaststore.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private String productId;
    private Integer quantity;
    private String name;
    private BigDecimal price;
    private String imgurl;
    private String category;
    private String description;

    public static CartItemResponse from(CartItem cartItem) {
        Product product = cartItem.getProduct();
        return new CartItemResponse(
                product != null ? product.getId() : null,
                cartItem.getQuantity(),
                product != null ? product.getName() : null,
                product != null ? product.getPrice() : null,
                product != null ? product.getImgurl() : null,
                product != null ? product.getCategory() : null,
                product != null ? product.getDescription() : null
        );
    }
}
