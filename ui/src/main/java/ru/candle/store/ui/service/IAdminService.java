package ru.candle.store.ui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.candle.store.ui.dto.request.AddProductRequest;
import ru.candle.store.ui.dto.request.AddPromocodeRequest;

public interface IAdminService {

    String showAllStaticImages(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    String deleteImage(String filename, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    String uploadFile(MultipartFile file, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    String showAllProducts(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    String addProduct(AddProductRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String deleteProduct(String productId, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String changeAvailableProduct(String productId, Boolean actual,Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String showAllPromocodes(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String addPromocode(AddPromocodeRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String changeAvailablePromocode(String promocode, Boolean actual,
                                    Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String showAllOrders(String status, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String getProduct(String orderId, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String changeProductSatus(String orderId, String status, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
}
