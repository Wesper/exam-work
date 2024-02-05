package ru.candle.store.productmplaceservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.candle.store.productmplaceservice.dto.IAvgRatingByProduct;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;
import ru.candle.store.productmplaceservice.impl.AvgRatingByProductImpl;
import ru.candle.store.productmplaceservice.repository.ProductRatingRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase
public class ProductRatingRepositoryTest {

    @Autowired
    private ProductRatingRepository ratingRepository;

    @Test
    void whenFindAllInWithEntities() {
        List<ProductRatingEntity> s = ratingRepository.findAll();
        List<ProductRatingEntity> expRatings = new ArrayList<>();
        expRatings.add(new ProductRatingEntity(9l, 5L, 5L, 5L));
        List<ProductRatingEntity> actRatings = ratingRepository.findAllIn(Arrays.asList(5L, 20L));
        Assertions.assertEquals(expRatings, actRatings);
    }

    @Test
    void whenFindAllInWithoutEntities() {
        List<ProductRatingEntity> expRatings = new ArrayList<>();
        List<ProductRatingEntity> actRatings = ratingRepository.findAllIn(Arrays.asList(10L, 20L));
        Assertions.assertEquals(expRatings, actRatings);
    }

    @Test
    void whenGetAvgRatingInGroupByProductWithEntities() {
        List<AvgRatingByProductImpl> expRatings = new ArrayList<>();
        expRatings.add(new AvgRatingByProductImpl(2.25, 1L));
        expRatings.add(new AvgRatingByProductImpl(3.0, 2L));
        List<IAvgRatingByProduct> actRatings = ratingRepository.getAvgRatingInGroupByProduct(Arrays.asList(1L, 2L));
        Assertions.assertAll(
                () -> Assertions.assertEquals(expRatings.get(0).getId(), actRatings.get(0).getId()),
                () -> Assertions.assertEquals(expRatings.get(0).getAverage(), actRatings.get(0).getAverage()),
                () -> Assertions.assertEquals(expRatings.get(1).getId(), actRatings.get(1).getId()),
                () -> Assertions.assertEquals(expRatings.get(1).getAverage(), actRatings.get(1).getAverage())
        );
    }

    @Test
    void whenGetAvgRatingInGroupByProductWithoutEntities() {
        List<AvgRatingByProductImpl> expRatings = new ArrayList<>();
        List<IAvgRatingByProduct> actRatings = ratingRepository.getAvgRatingInGroupByProduct(Arrays.asList(10L, 20L));
        Assertions.assertEquals(expRatings, actRatings);
    }

    @Test
    void whenFindByUserIdWithEntities() {
        Integer expRatings = 1;
        int actRatings = ratingRepository.findByUserIdAndProductId(1L, 1L);
        Assertions.assertEquals(expRatings, actRatings);
    }

    @Test
    void whenFindByUserIdWithoutEntities() {
        Integer expRatings = 0;
        int actRatings = ratingRepository.findByUserIdAndProductId(1L, 10L);
        Assertions.assertEquals(expRatings, actRatings);
    }

    @Test
    void whenGetAvgRatingByProductWithEntities() {
        Double expRatings = 4.5;
        Double actRatings = ratingRepository.getAvgRatingByProduct(3L);
        Assertions.assertEquals(expRatings, actRatings);
    }

    @Test
    void whenGetAvgRatingByProductWithoutEntities() {
        Double expRatings = null;
        Double actRatings = ratingRepository.getAvgRatingByProduct(30L);
        Assertions.assertEquals(expRatings, actRatings);
    }

}