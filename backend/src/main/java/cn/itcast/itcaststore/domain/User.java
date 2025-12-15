package cn.itcast.itcaststore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 2)
    private String gender;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String telephone;

    @Column(length = 500)
    private String introduce;

    @Column(length = 50)
    private String activeCode;

    @Column(nullable = false)
    private Integer state = 0; // 0:未激活 1:已激活

    @Column(nullable = false, length = 10)
    private String role = "普通用户"; // 普通用户/超级用户

    @Column(nullable = false, updatable = false)
    private LocalDateTime registTime = LocalDateTime.now();
}
