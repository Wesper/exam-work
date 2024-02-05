package ru.candle.store.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.candle.store.orderservice.entity.BasketEntity;
import ru.candle.store.orderservice.entity.ProductEntity;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<BasketEntity, Long> {

    Integer deleteByProductIdAndUserId(Long productId, Long userId);
    Boolean existByProductIdAndUserId(Long productId, Long userId);
    void deleteByUserId(Long userId);
    List<BasketEntity> findByUserId(Long userId);
    @Modifying
    @Query(value = "UPDATE orders.buskets SET count = :count WHERE user_id = :userId and product_id = :productId", nativeQuery = true)
    Integer updateCountByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId, @Param("count") Long count);
}
