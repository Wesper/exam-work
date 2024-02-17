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
import ru.candle.store.ui.dto.request.AddProductToBasketRequest;
import ru.candle.store.ui.dto.request.ApplyPromocodeRequest;
import ru.candle.store.ui.dto.request.DeleteBasketProductRequest;
import ru.candle.store.ui.dto.response.*;
import ru.candle.store.ui.exception.UIException;
import ru.candle.store.ui.service.IBasketService;

@Service
public class BasketServiceImpl implements IBasketService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Helper helper;

    @Value("${gateway.host}")
    private String gatewayUrl;

    @Value("${minio.images.url}")
    private String minioHost;

    @Override
    public String addProductToBasket(String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return addProductToBasketResponse(productId, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String getBasket(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return getBasketResponse(model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String deleteProductFromBasket(String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return deleteProductFromBasketResponse(productId, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String clearBasket(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return clearBasketResponse(model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String applyPromocodeToBasket(ApplyPromocodeRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return applyPromocodeToBasketResponse(request, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String getPreorder(String promocode, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return getPreorderResponse(promocode, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    private String getPreorderResponse(String promocode, Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<Void> entity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<GetProfileResponse> profileResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/profile/get", HttpMethod.GET, entity, GetProfileResponse.class);
        if (profileResponse.getBody() == null || !profileResponse.getBody().isSuccess()) {
            model.addAttribute("errorText", profileResponse.getBody().getErrorText());
            return "error";
        }
        if (profileResponse.getBody().getFirstName() == null
                || profileResponse.getBody().getLastName() == null
                || profileResponse.getBody().getAddress() == null) {
            model.addAttribute("errorText", "Для заказа необходимо заполнить в профиле Фамилию, Имя и Адрес");
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("firstName", profileResponse.getBody().getFirstName());
        model.addAttribute("lastName", profileResponse.getBody().getLastName());
        model.addAttribute("middleName", profileResponse.getBody().getMiddleName());
        model.addAttribute("address", profileResponse.getBody().getAddress());

        if (!promocode.isEmpty()) {
            ApplyPromocodeRequest request = ApplyPromocodeRequest.builder().promocode(promocode).build();
            HttpEntity<ApplyPromocodeRequest> applyPromocodeEntity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
            ResponseEntity<ApplyPromocodeResponse> basketProductResponseWithPromocode = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/promocode/apply", HttpMethod.POST, applyPromocodeEntity, ApplyPromocodeResponse.class);
            if (basketProductResponseWithPromocode.getBody() == null || !basketProductResponseWithPromocode.getBody().getSuccess()) {
                model.addAttribute("errorText", basketProductResponseWithPromocode.getBody().getErrorText());
                return "error";
            }
            model.addAttribute("promocode", request.getPromocode());
            model.addAttribute("minioHost", minioHost);
            model.addAttribute("products", basketProductResponseWithPromocode.getBody().getProducts());
            model.addAttribute("totalPrice", basketProductResponseWithPromocode.getBody().getTotalPrice());
            model.addAttribute("totalPromoPrice", basketProductResponseWithPromocode.getBody().getTotalPricePromo());
        } else {
            ResponseEntity<GetBasketResponse> basketProuctsResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/get", HttpMethod.GET, entity, GetBasketResponse.class);
            if (basketProuctsResponse.getBody() == null || !basketProuctsResponse.getBody().getSuccess()) {
                model.addAttribute("errorText", basketProuctsResponse.getBody().getErrorText());
                return "error";
            }
            model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
            model.addAttribute("minioHost", minioHost);
            model.addAttribute("products", basketProuctsResponse.getBody().getProducts());
            model.addAttribute("totalPrice", basketProuctsResponse.getBody().getTotalPrice());
        }

        return "preorder";
    }

    private String applyPromocodeToBasketResponse(ApplyPromocodeRequest request, Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<ApplyPromocodeRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<ApplyPromocodeResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/promocode/apply", HttpMethod.POST, entity, ApplyPromocodeResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("promocode", request.getPromocode());
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("minioHost", minioHost);
        model.addAttribute("products", response.getBody().getProducts());
        model.addAttribute("totalPrice", response.getBody().getTotalPrice());
        model.addAttribute("totalPromoPrice", response.getBody().getTotalPricePromo());
        return "basket";
    }

    private String clearBasketResponse(Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<Void> entity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<DeleteAllProductResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/clear", HttpMethod.GET, entity, DeleteAllProductResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../basket";
    }

    private String deleteProductFromBasketResponse(String productId, Model model, HttpServletRequest servletRequest) throws UIException {
        DeleteBasketProductRequest request = DeleteBasketProductRequest.builder().productId(Long.valueOf(productId)).build();
        HttpEntity<DeleteBasketProductRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<DeleteBasketProductResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/delete", HttpMethod.POST, entity, DeleteBasketProductResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../../../basket";
    }

    private String getBasketResponse(Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<Void> entity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<GetBasketResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/get", HttpMethod.GET, entity, GetBasketResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            if (response.getBody().getErrorCode().equals("PRODUCT_NOT_FOUND")) {
                model.addAttribute("productIsEmpty", "Список продуктов пуст");
                model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
                return "basket";
            } else {
                model.addAttribute("errorText", response.getBody().getErrorText());
                return "error";
            }
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("minioHost", minioHost);
        model.addAttribute("products", response.getBody().getProducts());
        model.addAttribute("totalPrice", response.getBody().getTotalPrice());
        return "basket";
    }

    private String addProductToBasketResponse(String productId, Model model, HttpServletRequest servletRequest) throws UIException {
        AddProductToBasketRequest request = AddProductToBasketRequest.builder().productId(Long.valueOf(productId)).count(1L).build();
        HttpEntity<AddProductToBasketRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<AddProductToBasketResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/add", HttpMethod.POST, entity, AddProductToBasketResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../../home";
    }

}
