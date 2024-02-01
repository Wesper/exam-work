package ru.candle.store.productmplaceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.candle.store.productmplaceservice.dto.request.AddProductRequest;
import ru.candle.store.productmplaceservice.dto.request.ChangeProductAvailableRequest;
import ru.candle.store.productmplaceservice.dto.request.GetProductCardRequest;
import ru.candle.store.productmplaceservice.dto.request.UpdateProductRequest;
import ru.candle.store.productmplaceservice.dto.response.*;
import ru.candle.store.productmplaceservice.entity.ProductEntity;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;
import ru.candle.store.productmplaceservice.entity.ProductReviewEntity;
import ru.candle.store.productmplaceservice.repository.ProductRatingRepository;
import ru.candle.store.productmplaceservice.repository.ProductRepository;
import ru.candle.store.productmplaceservice.repository.ProductReviewRepository;
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

    @Override
    public GetAllProductsResponse getAllProducts() {
        return getAllProductsResponse();
    }

    @Override
    public GetProductCardResponse getProductCard(GetProductCardRequest rq) {
        return getProductCardResponse(rq);
    }

    @Override
    public AddOrUpdateProductResponse addProduct(AddProductRequest rq) {
        return addProductResponse(rq);
    }

    @Override
    public AddOrUpdateProductResponse updateProduct(UpdateProductRequest rq) {
        return updateProductResponse(rq);
    }

    @Override
    public ChangeProductAvailableResponse changeProductAvailable(ChangeProductAvailableRequest rq) {
        return changeProductAvailableResponse(rq);
    }

    private GetAllProductsResponse getAllProductsResponse() {
        List<ProductEntity> products = productRepository.findAllByActual(true);
        List<Long> productIds = products.stream().map(ProductEntity::getId).collect(Collectors.toList());
        List<ProductRatingEntity> ratings = ratingRepository.findAllIn(productIds);
        return buildAllProductsResponse(products, ratings);
    }

    private GetAllProductsResponse buildAllProductsResponse(List<ProductEntity> productsEntity, List<ProductRatingEntity> ratingsEntity) {
        if (productsEntity == null || productsEntity.isEmpty()) {
            throw new RuntimeException("Не найдено ни одного продукта");
        }
        GetAllProductsResponse response = new GetAllProductsResponse();
        List<Product> products = new ArrayList<>();
        boolean f;
        long sum;
        long count;
        for (ProductEntity productEntity : productsEntity) {
            sum = 0;
            count = 0;
            if (ratingsEntity != null && !ratingsEntity.isEmpty()) {
                for (ProductRatingEntity ratingEntity : ratingsEntity) {
                    if (productEntity.getId().equals(ratingEntity.getProductId())) {
                        sum = ratingEntity.getSum();
                        count = ratingEntity.getCount();
                        break;
                    }
                }
            }
            long rating;
            if (sum == 0 || count == 0) {
                rating = 0;
            } else {
                rating = sum / count;
            }
            products.add(Product.builder()
                    .productId(productEntity.getId())
                    .imageId(productEntity.getImageId())
                    .title(productEntity.getTitle())
                    .subtitle(productEntity.getSubtitle())
                    .price(productEntity.getPrice())
                    .type(productEntity.getType())
                    .rating(rating)
                    .build()
            );
        }
        response.setSuccess(true);
        response.setProducts(products);
        return response;
    }

    private GetProductCardResponse getProductCardResponse(GetProductCardRequest rq) {
        ProductEntity product = productRepository.findById(rq.getProductId()).orElseThrow(() -> new RuntimeException("Продукт не найден"));
        ProductRatingEntity rating = ratingRepository.findByProductId(product.getId());
        List<ProductReviewEntity> review = reviewRepository.findAllByProductId(product.getId());
        return buildProductCardResponse(product, rating, review);
    }

    private GetProductCardResponse buildProductCardResponse(ProductEntity productEntity, ProductRatingEntity ratingEntity, List<ProductReviewEntity> reviewEntities) {
        List<Review> reviews = new ArrayList<>();
        if (reviewEntities != null && !reviewEntities.isEmpty()) {
            for (ProductReviewEntity review : reviewEntities) {
                reviews.add(Review.builder()
                        .userId(review.getUserId())
                        .review(review.getReview())
                        .build()
                );
            }
        }
        Long rating = ratingEntity == null || ratingEntity.getCount() == 0 || ratingEntity.getSum() == 0 ? 0 : ratingEntity.getSum()/ratingEntity.getCount();
        return GetProductCardResponse.builder()
                .productId(productEntity.getId())
                .imageId(productEntity.getImageId())
                .title(productEntity.getTitle())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .measure(productEntity.getMeasure())
                .type(productEntity.getType())
                .rating(rating)
                .actual(productEntity.getActual())
                .review(reviews)
                .build();
    }

    private AddOrUpdateProductResponse addProductResponse(AddProductRequest rq) {
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
        return new AddOrUpdateProductResponse(true);
    }

    private AddOrUpdateProductResponse updateProductResponse(UpdateProductRequest rq) {
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
        return new AddOrUpdateProductResponse(true);
    }

    @Transactional
    public ChangeProductAvailableResponse changeProductAvailableResponse(ChangeProductAvailableRequest rq) {
        int coutnUpdate = productRepository.updateActualById(rq.isActual(), rq.getProductId());
        if (coutnUpdate != 1) {
            throw new RuntimeException("Не удалось обновить доступность продукта");
        }
        return new ChangeProductAvailableResponse(true);
    }
}