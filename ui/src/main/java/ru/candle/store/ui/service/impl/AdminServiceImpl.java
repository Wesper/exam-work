package ru.candle.store.ui.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.candle.store.ui.components.Helper;
import ru.candle.store.ui.dictionary.Status;
import ru.candle.store.ui.dto.Image;
import ru.candle.store.ui.dto.request.*;
import ru.candle.store.ui.dto.response.*;
import ru.candle.store.ui.exception.UIException;
import ru.candle.store.ui.service.IAdminService;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Helper helper;

    @Value("${gateway.host}")
    private String gatewayUrl;

    @Value("${minio.images.url}")
    private String minioHost;

    @Override
    public String showAllStaticImages(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return showAllStaticImagesResponse(model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String deleteImage(String filename, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return deleteImageResponse(filename, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return uploadFileResponse(file, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String showAllProducts(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return showAllProductsResponse(model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String addProduct(AddProductRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return addProductResponse(request, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String deleteProduct(String productId, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return deleteProductResponse(productId, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String changeAvailableProduct(String productId, Boolean actual, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return changeAvailableProductResponse(productId, actual, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String showAllPromocodes(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return showAllPromocodesResponse(model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String addPromocode(AddPromocodeRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return addPromocodeResponse(request, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String changeAvailablePromocode(String promocode, Boolean actual, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return changeAvailablePromocodeResponse(promocode, actual, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String showAllOrders(String status, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return showAllOrdersResponse(status, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String getProduct(String orderId, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return getProductResponse(orderId, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String changeProductSatus(String orderId, String status, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return changeProductSatusResponse(orderId, status, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    private String changeProductSatusResponse(String orderId, String status, Model model, HttpServletRequest servletRequest) throws UIException {
        ChangeOrderStatusRequest request = ChangeOrderStatusRequest.builder().orderId(Long.valueOf(orderId)).status(Status.valueOf(status)).build();
        HttpEntity<ChangeOrderStatusRequest> basketEntity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<ChangeOrderStatusResponse> orderResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/order/status/change", HttpMethod.POST, basketEntity, ChangeOrderStatusResponse.class);
        if (orderResponse.getBody() == null || !orderResponse.getBody().getSuccess()) {
            model.addAttribute("errorText", orderResponse.getBody().getErrorText());
            return "error";
        }
        return "redirect:/admin/orders/list?status=" + status;
    }

    private String getProductResponse(String orderId, Model model, HttpServletRequest servletRequest) throws UIException {
        GetOrderRequest request = GetOrderRequest.builder().orderId(Long.valueOf(orderId)).build();
        HttpEntity<GetOrderRequest> basketEntity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<GetOrderResponse> orderResponse = restTemplate.exchange(gatewayUrl + "/gateway-server/order/get", HttpMethod.POST, basketEntity, GetOrderResponse.class);
        if (orderResponse.getBody() == null || !orderResponse.getBody().getSuccess()) {
            model.addAttribute("errorText", orderResponse.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("orderId", orderId);
        model.addAttribute("minioHost", minioHost);
        model.addAttribute("order", orderResponse.getBody());
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "adminOrderCard";
    }

    private String showAllOrdersResponse(String status, Model model, HttpServletRequest servletRequest) throws UIException {
        GetAllOrdersByStatusRequest request = GetAllOrdersByStatusRequest.builder().status(Status.valueOf(status)).build();
        HttpEntity<GetAllOrdersByStatusRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<GetAllOrdersByStatusResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/order/all/get", HttpMethod.POST, entity, GetAllOrdersByStatusResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            if (response.getBody().getErrorCode().equals("ORDER_IN_SEARCH_STATUS_NOT_FOUND")) {
                model.addAttribute("orderIsEmpty", "Заказы не найдены");
                model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
                return "adminOrdersList";
            } else {
                model.addAttribute("errorText", response.getBody().getErrorText());
                return "error";
            }
        }
        model.addAttribute("orders", response.getBody().getOrders());
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "adminOrdersList";
    }

    private String changeAvailablePromocodeResponse(String promocode, Boolean actual, Model model, HttpServletRequest servletRequest) throws UIException {
        ChangePromocodeActualRequest request = ChangePromocodeActualRequest.builder().promocode(promocode).actual(actual).build();
        HttpEntity<ChangePromocodeActualRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<ChangePromocodeActualResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/promocode/actual/change", HttpMethod.POST, entity, ChangePromocodeActualResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../list";
    }

    private String addPromocodeResponse(AddPromocodeRequest request, Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<AddPromocodeRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<AddPromocodeResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/promocode/add", HttpMethod.POST, entity, AddPromocodeResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:./list";
    }

    private String showAllPromocodesResponse(Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<GetAllPromocodesResponse> entity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<GetAllPromocodesResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/promocode/get", HttpMethod.GET, entity, GetAllPromocodesResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        if (response.getBody().getPromocodes().isEmpty()) {
            model.addAttribute("promocodeIsEmpty", "Список промокодов пуст");
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("promocodes", response.getBody().getPromocodes());
        return "promocodes";
    }

    private String changeAvailableProductResponse(String productId, Boolean actual, Model model, HttpServletRequest servletRequest) throws UIException {
        ChangeProductAvailableRequest request = ChangeProductAvailableRequest.builder().productId(Long.valueOf(productId)).actual(actual).build();
        HttpEntity<ChangeProductAvailableRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<ChangeProductAvailableResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/product/available/change", HttpMethod.POST, entity, ChangeProductAvailableResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../list";
    }

    private String deleteProductResponse(String productId, Model model, HttpServletRequest servletRequest) throws UIException {
        DeleteProductRequest request = DeleteProductRequest.builder().id(Long.valueOf(productId)).build();
        HttpEntity<DeleteProductRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<DeleteProductResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/product/delete", HttpMethod.POST, entity, DeleteProductResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../list";
    }

    private String addProductResponse(AddProductRequest request, Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<AddProductRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<AddProductResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/product/add", HttpMethod.POST, entity, AddProductResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:./list";
    }

    private String showAllProductsResponse(Model model, HttpServletRequest servletRequest) throws UIException {
        GetProductsInfoRequest request = GetProductsInfoRequest.builder().productId(new ArrayList<>()).build();
        HttpEntity<GetProductsInfoRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<GetProductsInfoResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/product/info/get", HttpMethod.POST, entity, GetProductsInfoResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            if (response.getBody().getErrorCode().equals("NOT_FOUND_BY_IDS")) {
                model.addAttribute("productIsEmpty", "Список продуктов пуст");
                model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
                return "products";
            } else {
                model.addAttribute("errorText", response.getBody().getErrorText());
                return "error";
            }
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("minioHost", minioHost);
        model.addAttribute("products", response.getBody().getProductsInfo());
        return "products";
    }

    private String uploadFileResponse(MultipartFile file, Model model, HttpServletRequest servletRequest) throws UIException, IOException {
        Image image = Image.builder()
                .file(file.getBytes())
                .contentType(file.getContentType())
                .filename(file.getOriginalFilename())
                .size(file.getSize())
                .build();

        HttpHeaders headers = helper.createAuthHeader(servletRequest);
        HttpEntity<Image> entity = new HttpEntity<>(image, headers);

        ResponseEntity<UploadImageResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/images/upload", HttpMethod.POST, entity, UploadImageResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:./list";
    }

    private String deleteImageResponse(String filename, Model model, HttpServletRequest servletRequest) throws UIException {
        DeleteImageRequest request = DeleteImageRequest.builder().imageName(filename).build();
        HttpEntity<DeleteImageRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<DeleteImageResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/images/delete", HttpMethod.POST, entity, DeleteImageResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../list";
    }

    private String showAllStaticImagesResponse(Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<Void> entity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<GetImagesListResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/images", HttpMethod.GET, entity, GetImagesListResponse.class);
        if (response.getBody() == null || !response.getBody().getSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("images", response.getBody().getImages());
        return "static";
    }

}
