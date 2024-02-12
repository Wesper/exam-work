package ru.candle.store.ui.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.ui.components.Helper;
import ru.candle.store.ui.dto.request.AddRatingRequest;
import ru.candle.store.ui.dto.request.AddReviewRequest;
import ru.candle.store.ui.dto.request.GetProductCardRequest;
import ru.candle.store.ui.dto.request.IsUserPurchasedProductRequest;
import ru.candle.store.ui.dto.response.*;
import ru.candle.store.ui.exception.UIException;
import ru.candle.store.ui.service.IMainService;

@Service
public class MainServiceImpl implements IMainService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Helper helper;

    @Value("${gateway.host}")
    private String gatewayUrl;

    @Value("${minio.images.url}")
    private String minioHost;

    @Override
    public String getHome(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return getHomeResponse(model, servletRequest);
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String getCard(String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return getCardResponse(productId, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String addReview(AddReviewRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return addReviewResponse(request, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String addRating(AddRatingRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return addRatingResponse(request, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    private String addRatingResponse(AddRatingRequest request, Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<AddRatingRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<AddRatingResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/product/rating/add", HttpMethod.POST, entity, AddRatingResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        return "redirect:/card/" + request.getProductId();
    }

    private String addReviewResponse(AddReviewRequest request, Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<AddReviewRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<AddOrderResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/product/review/add", HttpMethod.POST, entity, AddOrderResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        return "redirect:/card/" + request.getProductId();
    }

    private String getCardResponse(String productId, Model model, HttpServletRequest servletRequest) throws UIException {
        GetProductCardRequest productCardRequest = GetProductCardRequest.builder().productId(Long.valueOf(productId)).build();
        ResponseEntity<GetProductCardResponse> productCardResponse = restTemplate.postForEntity(gatewayUrl + "/gateway-server/product/card", productCardRequest, GetProductCardResponse.class);
        if (productCardResponse.getBody() == null || !productCardResponse.getBody().getSuccess()) {
            model.addAttribute("errorText", productCardResponse.getBody().getErrorText());
            return "error";
        }

        String role = helper.getCookiaValueByName(servletRequest, "role");
        if (!role.isBlank()) {
            ProductIsAppreciatedRequest appreciatedRequest = ProductIsAppreciatedRequest.builder().productId(Long.valueOf(productId)).build();
            HttpEntity<ProductIsAppreciatedRequest> appreciatedEntity = new HttpEntity<>(appreciatedRequest, helper.createAuthHeader(servletRequest));
            ResponseEntity<ProductIsAppreciatedResponse> appreciatedResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/product/appreciated", HttpMethod.POST, appreciatedEntity, ProductIsAppreciatedResponse.class);
            if (productCardResponse.getBody() == null || !productCardResponse.getBody().getSuccess()) {
                model.addAttribute("errorText", productCardResponse.getBody().getErrorText());
                return "error";
            }
            productCardResponse.getBody().setAppreciated(appreciatedResponse.getBody().getAppreciated());

            IsUserPurchasedProductRequest purchasedRequest = IsUserPurchasedProductRequest.builder().productId(Long.valueOf(productId)).build();
            HttpEntity<IsUserPurchasedProductRequest> entity = new HttpEntity<>(purchasedRequest, helper.createAuthHeader(servletRequest));
            ResponseEntity<IsUserPurchasedProductResponse> purchasedResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/order/purchased", HttpMethod.POST, entity, IsUserPurchasedProductResponse.class);
            if (productCardResponse.getBody() == null || !productCardResponse.getBody().getSuccess()) {
                model.addAttribute("errorText", productCardResponse.getBody().getErrorText());
                return "error";
            }
            model.addAttribute("isPurchased", purchasedResponse.getBody().getIsPurchased());
        } else {
            model.addAttribute("isPurchased", false);
        }
        model.addAttribute("product", productCardResponse.getBody());
        model.addAttribute("role", role);
        model.addAttribute("minioHost", minioHost);
        return "card";
    }

    private String getHomeResponse(Model model, HttpServletRequest servletRequest) {
        ResponseEntity<GetAllProductsResponse> response = restTemplate.getForEntity(gatewayUrl + "/gateway-server/product/get", GetAllProductsResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        if (response.getBody().getProducts().isEmpty()) {
            model.addAttribute("productIsEmpty", "Список продуктов пуст");
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("minioHost", minioHost);
        model.addAttribute("products", response.getBody().getProducts());
        return "home";
    }

}
