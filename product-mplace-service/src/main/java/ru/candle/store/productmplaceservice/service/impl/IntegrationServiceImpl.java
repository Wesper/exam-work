package ru.candle.store.productmplaceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.productmplaceservice.dto.request.IsUserPurchasedProductRequest;
import ru.candle.store.productmplaceservice.dto.response.IsUserPurchasedProductResponse;
import ru.candle.store.productmplaceservice.service.IIntegrationService;

@Service
public class IntegrationServiceImpl implements IIntegrationService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public boolean isUserPurchasedProduct(Long productId, Long userId) {
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(userId, productId);
        IsUserPurchasedProductResponse response = restTemplate.postForObject("http://order-service/order/isPurchase", request, IsUserPurchasedProductResponse.class);
        if (response != null) {
            return response.isSuccess();
        } else {
            throw new RuntimeException("Произошла ошибка при проверке продукта в списке пкупленных у пользователя");
        }
    }
}
