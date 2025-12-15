package cn.itcast.itcaststore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @Column(length = 100)
    private String id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 40)
    private String category;

    @Column(nullable = false)
    private Integer pnum; // 库存数量

    @Column(length = 255)
    private String imgurl;

    @Column(length = 1000)
    private String description;
}
