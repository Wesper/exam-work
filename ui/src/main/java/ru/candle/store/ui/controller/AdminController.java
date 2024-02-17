package ru.candle.store.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.candle.store.ui.dto.request.AddProductRequest;
import ru.candle.store.ui.dto.request.AddPromocodeRequest;
import ru.candle.store.ui.service.IAdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService service;

    @GetMapping("/images/list")
    public String showAllStaticImages(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.showAllStaticImages(model, servletRequest, servletResponse);
    }

    @GetMapping("/images/delete/{filename}")
    public String deleteImage(@PathVariable String filename, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.deleteImage(filename, model, servletRequest, servletResponse);
    }

    @PostMapping("/images/upload")
    public String uploadFile(@RequestPart("file") MultipartFile file, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.uploadFile(file, model, servletRequest, servletResponse);
    }

    @GetMapping("/products/list")
    public String showAllProducts(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.showAllProducts(model, servletRequest, servletResponse);
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute AddProductRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.addProduct(request, model, servletRequest, servletResponse);
    }

    @GetMapping("/products/delete/{productId}")
    public String deleteProduct(@PathVariable String productId, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.deleteProduct(productId, model, servletRequest, servletResponse);
    }

    @GetMapping("/products/change/available")
    public String changeAvailableProduct(@RequestParam("productId") String productId, @RequestParam("actual") Boolean actual,
                                         Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.changeAvailableProduct(productId, actual, model, servletRequest, servletResponse);
    }

    @GetMapping("/promocodes/list")
    public String showAllPromocodes(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.showAllPromocodes(model, servletRequest, servletResponse);
    }

    @PostMapping("/promocodes/add")
    public String addPromocode(@ModelAttribute AddPromocodeRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.addPromocode(request, model, servletRequest, servletResponse);
    }

    @GetMapping("/promocodes/change/available")
    public String changeAvailablePromocode(@RequestParam("promocode") String promocode, @RequestParam("actual") Boolean actual,
                                         Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.changeAvailablePromocode(promocode, actual, model, servletRequest, servletResponse);
    }

    @GetMapping("/orders/list")
    public String showAllOrders(@RequestParam("status") String status, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.showAllOrders(status, model, servletRequest, servletResponse);
    }

    @GetMapping("/orders/get/{orderId}")
    public String getProduct(@PathVariable String orderId, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.getProduct(orderId, model, servletRequest, servletResponse);
    }

    @PostMapping("/orders/status/change")
    public String changeProductSatus(@RequestParam("orderId") String orderId, @RequestParam("status") String status,
                                     Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.changeProductSatus(orderId, status, model, servletRequest, servletResponse);
    }
}
