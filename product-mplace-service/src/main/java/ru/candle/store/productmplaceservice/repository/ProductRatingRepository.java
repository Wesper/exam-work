package ru.candle.store.productmplaceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;

import java.util.List;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRatingEntity, Long> {

    @Query(value = "SELECT * FROM products.ratings WHERE product_id IN (:ids)", nativeQuery = true)
    List<ProductRatingEntity> findAllIn(@Param("ids") List<Long> ids);
    ProductRatingEntity findByProductId(Long productId);
}
