package ru.candle.store.productmplaceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.candle.store.productmplaceservice.dto.AvgRatingByProduct;
import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;
import ru.candle.store.productmplaceservice.entity.ProductEntity;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;
import ru.candle.store.productmplaceservice.entity.ProductReviewEntity;
import ru.candle.store.productmplaceservice.repository.ProductRatingRepository;
import ru.candle.store.productmplaceservice.repository.ProductRepository;
import ru.candle.store.productmplaceservice.repository.ProductReviewRepository;
import ru.candle.store.productmplaceservice.service.IIntegrationService;
import ru.candle.store.productmplaceservice.service.IProductService;
import ru.candle.store.productmplaceservice.service.ITransactionalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements IProductService, ITransactionalService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductRatingRepository ratingRepository;

    @Autowired
    ProductReviewRepository reviewRepository;

    @Autowired
    IIntegrationService integrationService;

    @Override
    public GetAllProductsResponse getAllProducts() {
        return getAllProductsResponse();
    }

    @Override
    public GetProductCardResponse getProductCard(GetProductCardRequest rq, Long userId) {
        return getProductCardResponse(rq, userId);
    }

    @Override
    public AddProductResponse addProduct(AddProductRequest rq) {
        return addProductResponse(rq);
    }

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest rq) {
        return updateProductResponse(rq);
    }

    @Override
    public ChangeProductAvailableResponse changeProductAvailable(ChangeProductAvailableRequest rq) {
        return changeProductAvailableResponse(rq);
    }

    @Override
    public AddReviewResponse addReview(AddReviewRequest rq, Long userId) {
        return addReviewResponse(rq, userId);
    }

    @Override
    public AddRatingResponse addRating(AddRatingRequest rq, Long userId) {
        return addRatingResponse(rq, userId);
    }

    private GetAllProductsResponse getAllProductsResponse() {
        List<ProductEntity> products = productRepository.findAllByActual(true);
        List<Long> productIds = products.stream().map(ProductEntity::getId).collect(Collectors.toList());
        List<AvgRatingByProduct> ratings = ratingRepository.getAvgRatingInGroupByProduct(productIds);
        return buildAllProductsResponse(products, ratings);
    }

    private GetAllProductsResponse buildAllProductsResponse(List<ProductEntity> productsEntity, List<AvgRatingByProduct> ratingsEntity) {
        if (productsEntity == null) {
            throw new RuntimeException("Не найдено ни одного продукта");
        }
        GetAllProductsResponse response = new GetAllProductsResponse();
        List<Product> products = new ArrayList<>();
        for (ProductEntity productEntity : productsEntity) {
            Product product = new Product(
                    productEntity.getId(),
                    productEntity.getImageId(),
                    productEntity.getTitle(),
                    productEntity.getSubtitle(),
                    productEntity.getPrice(),
                    productEntity.getType(),
                    null
            );
            Double rating = ratingsEntity.stream().filter(r -> r.getProductId().equals(productEntity.getId())).mapToDouble(AvgRatingByProduct::getAvgRating).sum();
            product.setRating(rating);
            products.add(product);
        }
        response.setSuccess(true);
        response.setProducts(products);
        return response;
    }

    private GetProductCardResponse getProductCardResponse(GetProductCardRequest rq, Long userId) {
        ProductEntity product = productRepository.findById(rq.getProductId()).orElseThrow(() -> new RuntimeException("Продукт не найден"));
        List<ProductReviewEntity> review = reviewRepository.findAllByProductId(product.getId());
        Double rating = ratingRepository.getAvgRatingByProduct(rq.getProductId());
        Integer countAppreciated = ratingRepository.findByUserId(userId);
        return buildProductCardResponse(product, rating, countAppreciated, review, userId);
    }

    private GetProductCardResponse buildProductCardResponse(ProductEntity productEntity, Double avgRating, Integer countAppreciated, List<ProductReviewEntity> reviewEntities, Long userId) {
        List<Review> reviews = new ArrayList<>();
        if (reviewEntities != null) {
            for (ProductReviewEntity review : reviewEntities) {
                reviews.add(Review.builder()
                        .userId(review.getUserId())
                        .review(review.getReview())
                        .build()
                );
            }
        }
        avgRating = avgRating != null ? avgRating : 0;
        boolean appreciated = countAppreciated > 0;

        return GetProductCardResponse.builder()
                .productId(productEntity.getId())
                .imageId(productEntity.getImageId())
                .title(productEntity.getTitle())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .measure(productEntity.getMeasure())
                .type(productEntity.getType())
                .rating(avgRating)
                .appreciated(appreciated)
                .actual(productEntity.getActual())
                .review(reviews)
                .build();
    }

    private AddProductResponse addProductResponse(AddProductRequest rq) {
        ProductEntity product = new ProductEntity(
                rq.getImageId(),
                rq.getTitle(),
                rq.getDescription(),
                rq.getSubtitle(),
                rq.getPrice(),
                rq.getType(),
                rq.getMeasure(),
                rq.getUnitMeasure(),
                rq.getActual()
        );
        productRepository.save(product);
        return new AddProductResponse(true);
    }

    private UpdateProductResponse updateProductResponse(UpdateProductRequest rq) {
        ProductEntity product = new ProductEntity(
                rq.getId(),
                rq.getImageId(),
                rq.getTitle(),
                rq.getDescription(),
                rq.getSubtitle(),
                rq.getPrice(),
                rq.getType(),
                rq.getMeasure(),
                rq.getUnitMeasure(),
                rq.getActual()
        );
        productRepository.save(product);
        return new UpdateProductResponse(true);
    }

    @Transactional
    protected ChangeProductAvailableResponse changeProductAvailableResponse(ChangeProductAvailableRequest rq) {
        int coutnUpdate = productRepository.updateActualById(rq.isActual(), rq.getProductId());
        if (coutnUpdate != 1) {
            throw new RuntimeException("Не удалось обновить доступность продукта");
        }
        return new ChangeProductAvailableResponse(true);
    }

    private AddReviewResponse addReviewResponse(AddReviewRequest rq, Long userId) {
        boolean isUserPurchaseProduct = integrationService.isUserPurchasedProduct(rq.getProductId(), userId);
        if (isUserPurchaseProduct) {
            ProductReviewEntity review = new ProductReviewEntity(userId, rq.getProductId(), rq.getReview());
            reviewRepository.save(review);
        } else {
            throw new RuntimeException("Нельзя оставить отзыв на не приобретенный продукт");
        }
        return new AddReviewResponse(true);
    }

    private AddRatingResponse addRatingResponse(AddRatingRequest rq, Long userId) {
        boolean isUserPurchaseProduct = integrationService.isUserPurchasedProduct(rq.getProductId(), userId);
        if (isUserPurchaseProduct) {
            ProductRatingEntity rating = new ProductRatingEntity(rq.getRating(), rq.getProductId(), userId);
            ratingRepository.save(rating);
        } else {
            throw new RuntimeException("Нельзя оценить не приобретенный продукт");
        }
        return new AddRatingResponse(true);
    }
}