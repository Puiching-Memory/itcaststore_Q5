package cn.itcast.itcaststore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemId implements Serializable {
    @Column(name = "order_id", length = 100)
    private String orderId;

    @Column(name = "product_id", length = 100)
    private String productId;
}
