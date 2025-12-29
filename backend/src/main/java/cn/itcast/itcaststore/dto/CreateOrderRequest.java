package cn.itcast.itcaststore.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {
    private BigDecimal money;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private List<OrderItemRequest> orderItems;

    @Data
    public static class OrderItemRequest {
        private String productId;
        private Integer buynum;
    }
}

