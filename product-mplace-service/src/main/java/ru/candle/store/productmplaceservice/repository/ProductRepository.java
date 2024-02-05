package ru.candle.store.productmplaceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.candle.store.productmplaceservice.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByActual(Boolean actual);
    @Modifying
    @Query(value = "UPDATE products.products SET actual = :actual WHERE id = :productId", nativeQuery = true)
    int updateActualById(@Param("actual") Boolean actual, @Param("productId") Long productId);
    @Query(value = "SELECT * FROM products.products WHERE id IN (:ids)", nativeQuery = true)
    List<ProductEntity> findByIds(@Param("ids") List<Long> ids);
}
