package cn.itcast.itcaststore.repository;

import cn.itcast.itcaststore.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByUserId(Long userId, Pageable pageable);
    List<Order> findByUserIdOrderByOrdertimeDesc(Long userId);
}
