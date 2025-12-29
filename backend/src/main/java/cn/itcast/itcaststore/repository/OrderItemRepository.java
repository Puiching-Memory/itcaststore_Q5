package cn.itcast.itcaststore.repository;

import cn.itcast.itcaststore.domain.OrderItem;
import cn.itcast.itcaststore.domain.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}

