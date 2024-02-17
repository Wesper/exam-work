package ru.candle.store.productmplaceservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.productmplaceservice.dictionary.ExceptionCode;
import ru.candle.store.productmplaceservice.dto.IAvgRatingByProduct;
import ru.candle.store.productmplaceservice.dto.Product;
import ru.candle.store.productmplaceservice.dto.Review;
import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;
import ru.candle.store.productmplaceservice.entity.ProductEntity;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;
import ru.candle.store.productmplaceservice.entity.ProductReviewEntity;
import ru.candle.store.productmplaceservice.exception.ProductMplaceException;
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
    void whenRequestAllProductsButThereNone() {
        Mockito.when(productRepository.findAllByActual(true)).thenReturn(null);

        GetAllProductsResponse response = productService.getAllProducts();
        Assertions.assertAll(
                () -> Assertions.assertTrue(response.getSuccess()),
                () -> Assertions.assertNull(response.getErrorCode()),
                () -> Assertions.assertNull(response.getErrorText()),
                () -> Assertions.assertEquals(new ArrayList<>(), response.getProducts())
        );
        Mockito.verify(productRepository, Mockito.times(1)).findAllByActual(true);
        Mockito.verify(ratingRepository, Mockito.times(0)).getAvgRatingInGroupByProduct(Mockito.any());
    }

    @Test
    void whenRequestAllProductsSuccess() {
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
                () -> Assertions.assertTrue(response.getSuccess()),
                () -> Assertions.assertEquals(2, response.getProducts().size()),
                () -> Assertions.assertEquals(expProducts.get(0), response.getProducts().get(0)),
                () -> Assertions.assertEquals(expProducts.get(1), response.getProducts().get(1))
        );
        Mockito.verify(productRepository, Mockito.times(1)).findAllByActual(true);
        Mockito.verify(ratingRepository, Mockito.times(1)).getAvgRatingInGroupByProduct(Mockito.any());
    }

    @Test
    void whenRequestProductCardButThereNone() {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(2L);
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));

        GetProductCardResponse response = productService.getProductCard(request);
        Assertions.assertAll(
                () -> Assertions.assertFalse(response.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.NOT_FOUND.getErrorCode(), response.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.NOT_FOUND.getErrorText(), response.getErrorText())
        );

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.times(0)).findAllByProductId(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(0)).getAvgRatingByProduct(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(0)).findByUserIdAndProductId(Mockito.any(), Mockito.any());
    }

    @Test
    void whenRequestProductCardSuccess() {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(2L);
        ProductEntity product = new ProductEntity(2L, "a.jpeg", "Product 2", "If you buy the product you win", "Best of product", 1L, "A", "1", "kg", true);
        List<ProductReviewEntity> reviews = new ArrayList<>();
        reviews.add(new ProductReviewEntity(1L, 2L, "Review text"));
        reviews.add(new ProductReviewEntity(2L, 2L, "Review text 2"));

        List<Review> expReviews = new ArrayList<>();
        expReviews.add(new Review(1L, "Review text"));
        expReviews.add(new Review(2L, "Review text 2"));
        GetProductCardResponse expProducts = new GetProductCardResponse(true, 2L, "a.jpeg", "Product 2", "If you buy the product you win", 1L, "1", "kg", "A", 2.5, true, false, expReviews, null, null);
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        Mockito.when(reviewRepository.findAllByProductId(Mockito.any())).thenReturn(reviews);
        Mockito.when(ratingRepository.getAvgRatingByProduct(Mockito.any())).thenReturn(2.5);

        GetProductCardResponse response = productService.getProductCard(request);
        Assertions.assertEquals(expProducts, response);

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByProductId(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(1)).getAvgRatingByProduct(Mockito.any());
    }

    @Test
    void whenRequestProductCardWithoutRatingAndReviewSuccess() {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(2L);
        ProductEntity product = new ProductEntity(2L, "a.jpeg", "Product 2", "If you buy the product you win", "Best of product", 1L, "A", "1", "kg", true);

        List<Review> expReviews = new ArrayList<>();
        GetProductCardResponse expProducts = new GetProductCardResponse(true, 2L, "a.jpeg", "Product 2", "If you buy the product you win", 1L, "1", "kg", "A", 0.0, true, false, expReviews, null, null);
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        Mockito.when(reviewRepository.findAllByProductId(Mockito.any())).thenReturn(new ArrayList<>());
        Mockito.when(ratingRepository.getAvgRatingByProduct(Mockito.any())).thenReturn(null);

        GetProductCardResponse response = productService.getProductCard(request);
        Assertions.assertEquals(expProducts, response);

        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByProductId(Mockito.any());
        Mockito.verify(ratingRepository, Mockito.times(1)).getAvgRatingByProduct(Mockito.any());
    }

    @Test
    void whenAddProductSuccess() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(Mockito.any());
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        AddProductResponse response = productService.addProduct(request);
        Assertions.assertTrue(response.getSuccess());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenAddProductFail() {
        Mockito.when(productRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        AddProductResponse response = productService.addProduct(request);

        Assertions.assertAll(
                () -> Assertions.assertFalse(response.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode(), response.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText(), response.getErrorText())
        );
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenUpdateProductSuccess() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(Mockito.any());
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        UpdateProductResponse response = productService.updateProduct(request);
        Assertions.assertTrue(response.getSuccess());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenUpdateProductFail() {
        Mockito.when(productRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        UpdateProductResponse response = productService.updateProduct(request);

        Assertions.assertAll(
                () -> Assertions.assertFalse(response.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode(), response.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText(), response.getErrorText())
        );
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void whenChangeProductAvailableSuccess() {
        Mockito.when(productRepository.updateActualById(Mockito.any(), Mockito.any())).thenReturn(1);
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, false);

        ChangeProductAvailableResponse response = productService.changeProductAvailable(request);
        Assertions.assertTrue(response.getSuccess());
        Mockito.verify(productRepository, Mockito.times(1)).updateActualById(Mockito.any(), Mockito.any());
    }

    @Test
    void whenChangeProductAvailableFail() {
        Mockito.when(productRepository.updateActualById(Mockito.any(), Mockito.any())).thenReturn(0);
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, false);
        ChangeProductAvailableResponse response = productService.changeProductAvailable(request);

        Assertions.assertAll(
                () -> Assertions.assertFalse(response.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.UPDATE_AVAILABLE_FAIL.getErrorCode(), response.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.UPDATE_AVAILABLE_FAIL.getErrorText(), response.getErrorText())
        );
        Mockito.verify(productRepository, Mockito.times(1)).updateActualById(Mockito.any(), Mockito.any());
    }

    @Test
    void whenAddReviewTrue() throws ProductMplaceException {
        Long userId = 1L;
        String role = "USER";
        ProductReviewEntity review = new ProductReviewEntity(1L, 1L, "review");
        Mockito.when(reviewRepository.save(review)).thenReturn(review);
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L, "USER")).thenReturn(true);
        AddReviewRequest request = new AddReviewRequest(1L, "review");

        AddReviewResponse response = productService.addReview(request, userId, role);
        Assertions.assertTrue(response.getSuccess());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(),
                Mockito.any(), Mockito.any());
    }

    @Test
    void whenAddReviewFalse() throws ProductMplaceException {
        Long userId = 1L;
        String role = "USER";
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L, "USER")).thenReturn(false);
        AddReviewRequest request = new AddReviewRequest(1L, "review");

        AddReviewResponse response = productService.addReview(request, userId, role);
        Assertions.assertAll(
                () -> Assertions.assertFalse(response.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.REVIEW_ACCESS_DENY.getErrorCode(), response.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.REVIEW_ACCESS_DENY.getErrorText(), response.getErrorText())
        );
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(),
                Mockito.any(), Mockito.any());
    }

    @Test
    void whenAddReviewFail() throws ProductMplaceException {
        Long userId = 1L;
        String role = "USER";
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L, "USER"))
                .thenThrow(new ProductMplaceException(ExceptionCode.CHECK_PURCHASED_FAIL, "Ошибка"));
        AddReviewRequest request = new AddReviewRequest(1L, "review");
        AddReviewResponse response = productService.addReview(request, userId, role);
        Assertions.assertAll(
                () -> Assertions.assertFalse(response.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.CHECK_PURCHASED_FAIL.getErrorCode(), response.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.CHECK_PURCHASED_FAIL.getErrorText(), response.getErrorText())
        );
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(),
                Mockito.any(), Mockito.any());
    }

    @Test
    void whenAddRatingSuccess() throws ProductMplaceException {
        Long userId = 1L;
        String role = "USER";
        ProductRatingEntity review = new ProductRatingEntity(1L, 1L, 1L);
        Mockito.when(ratingRepository.save(review)).thenReturn(review);
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L, "USER")).thenReturn(true);
        AddRatingRequest request = new AddRatingRequest(1L, 1L);

        AddRatingResponse response = productService.addRating(request, userId, role);
        Assertions.assertTrue(response.getSuccess());
        Mockito.verify(ratingRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(),
                Mockito.any(), Mockito.any());
    }

    @Test
    void whenAddRatingFail() throws ProductMplaceException {
        Long userId = 1L;
        String role = "USER";
        ProductRatingEntity review = new ProductRatingEntity(1L, 1L, 1L);
        Mockito.when(integrationService.isUserPurchasedProduct(1L, 1L, "USER")).thenReturn(false);
        AddRatingRequest request = new AddRatingRequest(1L, 1L);

        AddRatingResponse response = productService.addRating(request, userId, role);
        Assertions.assertAll(
                () -> Assertions.assertFalse(response.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.RATING_ACCESS_DENY.getErrorCode(), response.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.RATING_ACCESS_DENY.getErrorText(), response.getErrorText())
        );
        Mockito.verify(ratingRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(integrationService, Mockito.times(1)).isUserPurchasedProduct(Mockito.any(),
                Mockito.any(), Mockito.any());
    }


}
