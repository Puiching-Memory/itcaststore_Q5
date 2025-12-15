package cn.itcast.itcaststore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 2000)
    private String details;

    @Column(name = "n_time", length = 18)
    private String time;
}
