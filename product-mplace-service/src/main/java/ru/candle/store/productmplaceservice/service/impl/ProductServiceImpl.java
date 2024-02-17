package ru.candle.store.productmplaceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.candle.store.productmplaceservice.dictionary.ExceptionCode;
import ru.candle.store.productmplaceservice.dto.IAvgRatingByProduct;
import ru.candle.store.productmplaceservice.dto.Product;
import ru.candle.store.productmplaceservice.dto.ProductInfo;
import ru.candle.store.productmplaceservice.dto.Review;
import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;
import ru.candle.store.productmplaceservice.entity.ProductEntity;
import ru.candle.store.productmplaceservice.entity.ProductRatingEntity;
import ru.candle.store.productmplaceservice.entity.ProductReviewEntity;
import ru.candle.store.productmplaceservice.exception.ProductMplaceException;
import ru.candle.store.productmplaceservice.repository.ProductRatingRepository;
import ru.candle.store.productmplaceservice.repository.ProductRepository;
import ru.candle.store.productmplaceservice.repository.ProductReviewRepository;
import ru.candle.store.productmplaceservice.service.IIntegrationService;
import ru.candle.store.productmplaceservice.service.IProductService;
import ru.candle.store.productmplaceservice.service.ITransactionalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        try {
            return getAllProductsResponse();
        } catch (ProductMplaceException e) {
            log.warn(e.getMessage());
            return GetAllProductsResponse.builder()
                    .success(true)
                    .products(new ArrayList<>())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return GetAllProductsResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public GetProductCardResponse getProductCard(GetProductCardRequest rq) {
        try {
            return getProductCardResponse(rq);
        } catch (ProductMplaceException e) {
            log.error(e.getMessage(), e);
            return GetProductCardResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return GetProductCardResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public AddProductResponse addProduct(AddProductRequest rq) {
        try {
            return addProductResponse(rq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AddProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest rq) {
        try {
            return updateProductResponse(rq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return UpdateProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public ChangeProductAvailableResponse changeProductAvailable(ChangeProductAvailableRequest rq) {
        try {
            return changeProductAvailableResponse(rq);
        } catch (ProductMplaceException e) {
            log.error(e.getMessage(), e);
            return ChangeProductAvailableResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ChangeProductAvailableResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public AddReviewResponse addReview(AddReviewRequest rq, Long userId, String role) {
        try {
            return addReviewResponse(rq, userId, role);
        } catch (ProductMplaceException e) {
            log.error(e.getMessage(), e);
            return AddReviewResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AddReviewResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public AddRatingResponse addRating(AddRatingRequest rq, Long userId, String role) {
        try {
            return addRatingResponse(rq, userId, role);
        } catch (ProductMplaceException e) {
            log.error(e.getMessage(), e);
            return AddRatingResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AddRatingResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public GetProductsInfoResponse getProductInfoByIds(GetProductsInfoRequest rq) {
        try {
            return getProductInfoResponse(rq);
        } catch (ProductMplaceException e) {
            log.error(e.getMessage(), e);
            return GetProductsInfoResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return GetProductsInfoResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public DeleteProductResponse deleteProduct(DeleteProductRequest rq) {
        try {
            return deleteProductResponse(rq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return DeleteProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public ProductIsAppreciatedResponse productIsAppreciated(ProductIsAppreciatedRequest rq, Long userId) {
        try {
            return productIsAppreciatedResponse(rq, userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ProductIsAppreciatedResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    private ProductIsAppreciatedResponse productIsAppreciatedResponse(ProductIsAppreciatedRequest rq, Long userId) {
        Integer countAppreciated = ratingRepository.findByUserIdAndProductId(userId, rq.getProductId());
        return ProductIsAppreciatedResponse.builder().success(true).appreciated(countAppreciated > 0).build();
    }

    private GetAllProductsResponse getAllProductsResponse() throws ProductMplaceException {
        List<ProductEntity> products = productRepository.findAllByActual(true);
        if (products == null) {
            throw new ProductMplaceException(ExceptionCode.NOT_FOUND_ALL, "Не найдено ни одного продукта");
        }
        List<Long> productIds = products.stream().map(ProductEntity::getId).collect(Collectors.toList());
        List<IAvgRatingByProduct> ratings = ratingRepository.getAvgRatingInGroupByProduct(productIds);
        return buildAllProductsResponse(products, ratings);
    }

    private GetAllProductsResponse buildAllProductsResponse(List<ProductEntity> productsEntity, List<IAvgRatingByProduct> ratingsEntity) {
        GetAllProductsResponse response = new GetAllProductsResponse();
        List<Product> products = new ArrayList<>();
        Double rating;
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
            rating = ratingsEntity.stream().filter(r -> r.getId().equals(productEntity.getId())).mapToDouble(IAvgRatingByProduct::getAverage).sum();
            product.setRating(rating);
            products.add(product);
        }
        response.setSuccess(true);
        response.setProducts(products);
        return response;
    }

    private GetProductCardResponse getProductCardResponse(GetProductCardRequest rq) throws ProductMplaceException {
        ProductEntity product = productRepository.findById(rq.getProductId()).orElseThrow(() -> new ProductMplaceException(ExceptionCode.NOT_FOUND, "Продукт не найден " + rq.getProductId()));
        List<ProductReviewEntity> review = reviewRepository.findAllByProductId(product.getId());
        Double rating = ratingRepository.getAvgRatingByProduct(rq.getProductId());
        return buildProductCardResponse(product, rating, review);
    }

    private GetProductCardResponse buildProductCardResponse(ProductEntity productEntity, Double avgRating, List<ProductReviewEntity> reviewEntities) {
        List<Review> reviews = new ArrayList<>();
        if (!reviewEntities.isEmpty()) {
            for (ProductReviewEntity review : reviewEntities) {
                reviews.add(Review.builder()
                        .userId(review.getUserId())
                        .review(review.getReview())
                        .build()
                );
            }
        }
        avgRating = avgRating != null ? avgRating : 0;

        return GetProductCardResponse.builder()
                .success(true)
                .productId(productEntity.getId())
                .imageId(productEntity.getImageId())
                .title(productEntity.getTitle())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .measure(productEntity.getMeasure())
                .unitMeasure(productEntity.getUnitMeasure())
                .type(productEntity.getType())
                .rating(avgRating)
                .appreciated(false)
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
        return AddProductResponse.builder().success(true).build();
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
        return UpdateProductResponse.builder().success(true).build();
    }

    @Transactional
    protected ChangeProductAvailableResponse changeProductAvailableResponse(ChangeProductAvailableRequest rq) throws ProductMplaceException {
        int coutnUpdate = productRepository.updateActualById(rq.isActual(), rq.getProductId());
        if (coutnUpdate != 1) {
            throw new ProductMplaceException(ExceptionCode.UPDATE_AVAILABLE_FAIL, "Не удалось обновить доступность продукта " + rq.getProductId());
        }
        return ChangeProductAvailableResponse.builder().success(true).build();
    }

    private AddReviewResponse addReviewResponse(AddReviewRequest rq, Long userId, String role) throws ProductMplaceException {
        boolean isUserPurchaseProduct = integrationService.isUserPurchasedProduct(rq.getProductId(), userId, role);
        if (isUserPurchaseProduct) {
            ProductReviewEntity review = new ProductReviewEntity(userId, rq.getProductId(), rq.getReview());
            reviewRepository.save(review);
        } else {
            throw new ProductMplaceException(ExceptionCode.REVIEW_ACCESS_DENY, "Нельзя оставить отзыв на не приобретенный продукт");
        }
        return AddReviewResponse.builder().success(true).build();
    }

    private AddRatingResponse addRatingResponse(AddRatingRequest rq, Long userId, String role) throws ProductMplaceException {
        boolean isUserPurchaseProduct = integrationService.isUserPurchasedProduct(rq.getProductId(), userId, role);
        if (isUserPurchaseProduct) {
            ProductRatingEntity rating = new ProductRatingEntity(rq.getRating(), rq.getProductId(), userId);
            ratingRepository.save(rating);
        } else {
            throw new ProductMplaceException(ExceptionCode.RATING_ACCESS_DENY, "Нельзя оценить не приобретенный продукт");
        }
        return AddRatingResponse.builder().success(true).build();
    }

    public GetProductsInfoResponse getProductInfoResponse(GetProductsInfoRequest rq) throws ProductMplaceException {
        List<ProductEntity> products = new ArrayList<>();
        if (rq.getProductId().isEmpty()) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByIds(rq.getProductId());
        }
        if (products.isEmpty()) {
            throw new ProductMplaceException(ExceptionCode.NOT_FOUND_BY_IDS, "Продукты с указанным ID не найдены");
        }
        List<ProductInfo> productsInfo = new ArrayList<>();
        products.forEach(product -> {
            productsInfo.add(new ProductInfo(
                    product.getId(),
                    product.getImageId(),
                    product.getTitle(),
                    product.getDescription(),
                    product.getSubtitle(),
                    product.getPrice(),
                    product.getType(),
                    product.getMeasure(),
                    product.getUnitMeasure(),
                    product.getActual()
            ));
        });
        return GetProductsInfoResponse.builder()
                .success(true)
                .productsInfo(productsInfo)
                .build();
    }

    private DeleteProductResponse deleteProductResponse(DeleteProductRequest rq) {
        productRepository.deleteById(rq.getId());
        return DeleteProductResponse.builder().success(true).build();
    }
}