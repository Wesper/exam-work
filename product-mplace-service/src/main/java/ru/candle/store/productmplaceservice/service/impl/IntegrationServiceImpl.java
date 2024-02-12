package ru.candle.store.productmplaceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.productmplaceservice.dictionary.ExceptionCode;
import ru.candle.store.productmplaceservice.dto.request.IsUserPurchasedProductRequest;
import ru.candle.store.productmplaceservice.dto.response.IsUserPurchasedProductResponse;
import ru.candle.store.productmplaceservice.exception.ProductMplaceException;
import ru.candle.store.productmplaceservice.service.IIntegrationService;

@Slf4j
@Service
public class IntegrationServiceImpl implements IIntegrationService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public boolean isUserPurchasedProduct(Long productId, Long userId, String role) throws ProductMplaceException {
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(userId, productId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        headers.set("userId", String.valueOf(userId));
        HttpEntity<IsUserPurchasedProductRequest> entity = new HttpEntity<>(request, headers);
        IsUserPurchasedProductResponse response = restTemplate.postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class);
        if (response != null && response.getSuccess().equals(true)) {
            return response.getIsPurchased();
        } else {
            log.error("Произошла ошибка при проверке продукта в списке купленных у пользователя " + response);
            throw new ProductMplaceException(ExceptionCode.CHECK_PURCHASED_FAIL, "Произошла ошибка при проверке продукта в списке купленных у пользователя");
        }
    }
}