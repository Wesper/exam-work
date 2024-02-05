package ru.candle.store.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.candle.store.orderservice.entity.PromocodeEntity;

@Repository
public interface PromocodeRepository extends JpaRepository<PromocodeEntity, String> {

    PromocodeEntity findByPromocode(String promocode);
}
