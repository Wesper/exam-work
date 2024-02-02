package ru.candle.store.productmplaceservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.productmplaceservice.dto.IAvgRatingByProduct;
import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;
import ru.candle.store.productmplaceservice.entity.ProductEntity;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;
import ru.candle.store.productmplaceservice.entity.ProductReviewEntity;
import ru.candle.store.productmplaceservice.impl.AvgRatingByProductImpl;
import ru.candle.store.productmplaceservice.repository.ProductRatingRepository;
import ru.candle.store.productmplaceservice.repository.ProductRepository;
import ru.candle.store.productmplaceservice.repository.ProductReviewRepository;
import ru.candle.store.productmplaceservice.service.IIntegrationService;
import ru.candle.store.productmplaceservice.service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRatingRepository ratingRepository;

    @Mock
    private ProductReviewRepository reviewRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    IIntegrationService integrationService;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void WhenRequestAllProductsButThereNone() {
        Mockito.when(productRepository.findAllByActual(true)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> productService.getAllProducts());
        Mockito.verify(productRepository, Mockito.times(1)).findAllByActual(true);
        Mockito.verify(ratingRepository, Mockito.times(0)).getAvgRatingInGroupByProduct(Mockito.any());
    }

    @Test
    void WhenRequestAllProductsSuccess() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(new ProductEntity(1L, "a.jpeg", "Product 1", "If you buy the product you win", "Best of product", 1L, "A", "1", "kg", true));
        products.add(new ProductEntity(2L, "b.jpeg", "Product 2", "If you buy the product you win", "Best of product", 2L, "B", "2", "kg", true));
        List<IAvgRatingByProduct> ratings = new ArrayList<>();
        ratings.add(new AvgRatingByProductImpl(2.5, 1L));
        List<Product> expProducts = new ArrayList<>();
        expProducts.add(new Product(1L, "a.jpeg", "Product 1", "Best of product", 1L, "A", 2.5));
        expProducts.add(new Product(2L, "b.jpeg", "Product 2", "Best of product", 2L, "B", 0.0));

        Mockito.when(productRepository.findAllByActual(true)).thenReturn(products);
        Mockito.when(ratingRepository.getAvgRatingInGroupByProduct(Mockito.any())).thenReturn(ratings);

        GetAllProductsResponse response = productService.getAllProducts();
        Assertions.assertAll(
                () -> Assertions.assertTrue(response.isSuccess()),
                () -> Assertions.assertEquals(2, response.getProducts().size()),
                () -> Assertions.assertEquals(expProducts.get(0), response.getProducts().get(0)),
                () -> Assertions.assertEquals(expProducts.get(1), response.getProducts().get(1))
        );
        Mockito.verify(productRepository, Mockito.times(1)).findAllByActual(true);
        Mockito.verify(ratingRepository, Mockito.times(1)).getAvgRatingInGroupByProduct(Mockito.any());
    }

    @Test
    void WhenRequestProductCardButThereNone() {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(2L);
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> productService.getProductCard(request, userId));
        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.times(0)).findAllByProductId(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(0)).getAvgRatingByProduct(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(0)).findByUserIdAndProductId(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenRequestProductCardSuccess() {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(2L);
        ProductEntity product = new ProductEntity(2L, "a.jpeg", "Product 2", "If you buy the product you win", "Best of product", 1L, "A", "1", "kg", true);
        List<ProductReviewEntity> reviews = new ArrayList<>();
        reviews.add(new ProductReviewEntity(1L, 2L, "Review text"));
        reviews.add(new ProductReviewEntity(2L, 2L, "Review text 2"));

        List<Review> expReviews = new ArrayList<>();
        expReviews.add(new Review(1L, "Review text"));
        expReviews.add(new Review(2L, "Review text 2"));
        GetProductCardResponse expProducts = new GetProductCardResponse(2L, "a.jpeg", "Product 2", "If you buy the product you win", 1L, "1", "kg", "A", 2.5, true, true, expReviews);
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        Mockito.when(reviewRepository.findAllByProductId(Mockito.any())).thenReturn(reviews);
        Mockito.when(ratingRepository.getAvgRatingByProduct(Mockito.any())).thenReturn(2.5);
        Mockito.when(ratingRepository.findByUserIdAndProductId(Mockito.any(), Mockito.any())).thenReturn(1);

        GetProductCardResponse response = productService.getProductCard(request, userId);
        Assertions.assertEquals(expProducts, response);

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByProductId(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(1)).getAvgRatingByProduct(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(1)).findByUserIdAndProductId(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenRequestProductCardWithoutRatingAndReviewSuccess() {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(2L);
        ProductEntity product = new ProductEntity(2L, "a.jpeg", "Product 2", "If you buy the product you win", "Best of product", 1L, "A", "1", "kg", true);

        List<Review> expReviews = new ArrayList<>();
        GetProductCardResponse expProducts = new GetProductCardResponse(2L, "a.jpeg", "Product 2", "If you buy the product you win", 1L, "1", "kg", "A", 0.0, true, false, expReviews);
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        Mockito.when(reviewRepository.findAllByProductId(Mockito.any())).thenReturn(null);
        Mockito.when(ratingRepository.getAvgRatingByProduct(Mockito.any())).thenReturn(null);
        Mockito.when(ratingRepository.findByUserIdAndProductId(Mockito.any(), Mockito.any())).thenReturn(0);

        GetProductCardResponse response = productService.getProductCard(request, userId);
        Assertions.assertEquals(expProducts, response);

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByProductId(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(1)).getAvgRatingByProduct(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(1)).findByUserIdAndProductId(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenAddProductSuccess() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(Mockito.any());
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        AddProductResponse response = productService.addProduct(request);
        Assertions.assertTrue(response.isSuccess());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void WhenAddProductFail() {
        Mockito.when(productRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.addProduct(request));
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void WhenUpdateProductSuccess() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(Mockito.any());
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        UpdateProductResponse response = productService.updateProduct(request);
        Assertions.assertTrue(response.isSuccess());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void WhenUpdateProductFail() {
        Mockito.when(productRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(request));
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void WhenChangeProductAvailableSuccess() {
        Mockito.when(productRepository.updateActualById(Mockito.any(), Mockito.any())).thenReturn(1);
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, false);

        ChangeProductAvailableResponse response = productService.changeProductAvailable(request);
        Assertions.assertTrue(response.isSuccess());
        Mockito.verify(productRepository, Mockito.times(1)).updateActualById(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenChangeProductAvailableFail() {
        Mockito.when(productRepository.updateActualById(Mockito.any(), Mockito.any())).thenReturn(0);
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, false);

        Assertions.assertThrows(RuntimeException.class, () -> productService.changeProductAvailable(request));
        Mockito.verify(productRepository, Mockito.times(1)).updateActualById(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenAddReviewSuccess() {
        Long userId = 1L;
        ProductReviewEntity review = new ProductReviewEntity(1L, 1L, "review");
        Mockito.when(reviewRepository.save(review)).thenReturn(review);
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L)).thenReturn(true);
        AddReviewRequest request = new AddReviewRequest(1L, "review");

        AddReviewResponse response = productService.addReview(request, userId);
        Assertions.assertTrue(response.isSuccess());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenAddReviewFail() {
        Long userId = 1L;
        ProductReviewEntity review = new ProductReviewEntity(1L, 1L, "review");
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L)).thenReturn(false);
        AddReviewRequest request = new AddReviewRequest(1L, "review");

        Assertions.assertThrows(RuntimeException.class, () -> productService.addReview(request, userId));
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenAddRatingSuccess() {
        Long userId = 1L;
        ProductRatingEntity review = new ProductRatingEntity(1L, 1L, 1L);
        Mockito.when(ratingRepository.save(review)).thenReturn(review);
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L)).thenReturn(true);
        AddRatingRequest request = new AddRatingRequest(1L, 1L);

        AddRatingResponse response = productService.addRating(request, userId);
        Assertions.assertTrue(response.isSuccess());
        Mockito.verify(ratingRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(), Mockito.any());
    }

    @Test
    void WhenAddRatingFail() {
        Long userId = 1L;
        ProductRatingEntity review = new ProductRatingEntity(1L, 1L, 1L);
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L)).thenReturn(false);
        AddRatingRequest request = new AddRatingRequest(1L, 1L);

        Assertions.assertThrows(RuntimeException.class, () -> productService.addRating(request, userId));
        Mockito.verify(ratingRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(), Mockito.any());
    }


}
