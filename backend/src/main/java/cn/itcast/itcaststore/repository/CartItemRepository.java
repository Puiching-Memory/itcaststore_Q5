package cn.itcast.itcaststore.repository;

import cn.itcast.itcaststore.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select ci from CartItem ci join fetch ci.product where ci.userId = :userId order by ci.createdAt asc")
    List<CartItem> findWithProductByUserId(@Param("userId") Long userId);

    Optional<CartItem> findByUserIdAndProductId(Long userId, String productId);

    void deleteByUserIdAndProductId(Long userId, String productId);

    void deleteByUserId(Long userId);
}
