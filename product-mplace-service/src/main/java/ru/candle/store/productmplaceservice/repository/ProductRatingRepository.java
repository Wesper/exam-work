package ru.candle.store.productmplaceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.candle.store.productmplaceservice.dto.IAvgRatingByProduct;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;

import java.util.List;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRatingEntity, Long> {

    @Query(value = "SELECT * FROM products.ratings WHERE product_id IN (:ids)", nativeQuery = true)
    List<ProductRatingEntity> findAllIn(@Param("ids") List<Long> ids);

    @Query(value = "SELECT AVG(rating) as average, product_id as id FROM products.ratings WHERE product_id IN (:ids) GROUP BY product_id", nativeQuery = true)
    List<IAvgRatingByProduct> getAvgRatingInGroupByProduct(@Param("ids") List<Long> ids);

    @Query(value = "SELECT COUNT(*) FROM products.ratings WHERE user_id = :userId and product_id = :productId", nativeQuery = true)
    Integer findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query(value = "SELECT AVG(rating) FROM products.ratings WHERE product_id = :productId", nativeQuery = true)
    Double getAvgRatingByProduct(@Param("productId") Long productId);

}