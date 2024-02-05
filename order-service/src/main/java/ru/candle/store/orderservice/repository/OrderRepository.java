package ru.candle.store.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.entity.OrderEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity findByIdAndUserId(Long id, Long userId);
    List<OrderEntity> findAllByStatus(Status status);
    List<OrderEntity> findAllByUserId(Long userId);
    void deleteAllByUserId(Long userId);

}
