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
import ru.candle.store.ui.dto.ProductAndCount;
import ru.candle.store.ui.dto.request.AddOrderRequest;
import ru.candle.store.ui.dto.request.GetOrderRequest;
import ru.candle.store.ui.dto.response.AddOrderResponse;
import ru.candle.store.ui.dto.response.GetBasketResponse;
import ru.candle.store.ui.dto.response.GetOrderListResponse;
import ru.candle.store.ui.dto.response.GetOrderResponse;
import ru.candle.store.ui.exception.UIException;
import ru.candle.store.ui.service.IOrderService;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Helper helper;

    @Value("${gateway.host}")
    private String gatewayUrl;

    @Value("${minio.images.url}")
    private String minioHost;

    @Override
    public String createOrder(String promocode, String address, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return createOrderResponse(promocode, address, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String getUserOrders(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return getUSerOrdersResponse(model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String getUserOrder(String orderId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        try {
            return getUserOrderResponse(orderId, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    private String getUserOrderResponse(String orderId, Model model, HttpServletRequest servletRequest) throws UIException {
        GetOrderRequest request = GetOrderRequest.builder().orderId(Long.valueOf(orderId)).build();
        HttpEntity<GetOrderRequest> basketEntity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<GetOrderResponse> orderResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/order/get", HttpMethod.POST, basketEntity, GetOrderResponse.class);
        if (orderResponse.getBody() == null || !orderResponse.getBody().getSuccess()) {
            model.addAttribute("errorText", orderResponse.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("minioHost", minioHost);
        model.addAttribute("order", orderResponse.getBody());
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "userOrderCard";
    }

    private String getUSerOrdersResponse(Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<Void> basketEntity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<GetOrderListResponse> ordersResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/order/get", HttpMethod.GET, basketEntity, GetOrderListResponse.class);
        if (ordersResponse.getBody() == null || !ordersResponse.getBody().getSuccess()) {
            if (ordersResponse.getBody().getErrorCode().equals("USER_DONT_HAVE_ORDERS")) {
                model.addAttribute("orderIsEmpty", "Список заказов пуст");
                model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
                return "userOrdersList";
            } else {
                model.addAttribute("errorText", ordersResponse.getBody().getErrorText());
                return "error";
            }
        }
        model.addAttribute("orders", ordersResponse.getBody().getOrders());
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "userOrdersList";
    }

    private String createOrderResponse(String promocode, String address, Model model, HttpServletRequest servletRequest) throws UIException {
        AddOrderRequest request = new AddOrderRequest();
        request.setProductsAndCounts(new ArrayList<>());
        request.setAddress(address);
        if (!promocode.isEmpty()) {
            request.setPromocode(promocode);
        }
        HttpEntity<Void> basketEntity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<GetBasketResponse> basketProductsResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/basket/get", HttpMethod.GET, basketEntity, GetBasketResponse.class);
        if (basketProductsResponse.getBody() == null || !basketProductsResponse.getBody().getSuccess()) {
            model.addAttribute("errorText", basketProductsResponse.getBody().getErrorText());
            return "error";
        }
        HashMap<Long, Long> products = new HashMap<>();
        basketProductsResponse.getBody().getProducts().forEach(basketProduct -> {
            if (products.containsKey(basketProduct.getProductId())) {
                products.put(basketProduct.getProductId(), products.get(basketProduct.getProductId()) + 1L);
            } else {
                products.put(basketProduct.getProductId(), basketProduct.getCount());
            }
        });
        products.forEach((id, count) -> request.getProductsAndCounts().add(new ProductAndCount(id, count)));
        HttpEntity<AddOrderRequest> orderEntity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<AddOrderResponse> orderResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/order/add", HttpMethod.POST, orderEntity, AddOrderResponse.class);
        if (orderResponse.getBody() == null || !orderResponse.getBody().getSuccess()) {
            model.addAttribute("errorText", orderResponse.getBody().getErrorText());
            return "error";
        }
        return "redirect:./list";
    }


}
