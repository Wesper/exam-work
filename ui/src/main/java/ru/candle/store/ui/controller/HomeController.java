package ru.candle.store.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.ui.dto.request.AddRatingRequest;
import ru.candle.store.ui.dto.request.AddReviewRequest;
import ru.candle.store.ui.service.IMainService;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private IMainService service;

    @GetMapping
    public String homeRedirect() {
        return "redirect:/home";
    }

    @GetMapping(value = "/home")
    public String getHome(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.getHome(model, servletResponse, servletRequest);
    }

    @GetMapping("/card/{productId}")
    public String getCard(@PathVariable String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.getCard(productId, model, servletResponse, servletRequest);
    }

    @PostMapping("/review/add")
    public String addReview(@ModelAttribute AddReviewRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.addReview(request, model, servletResponse, servletRequest);
    }

    @PostMapping("/rating/add")
    public String addRating(@ModelAttribute AddRatingRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.addRating(request, model, servletResponse, servletRequest);
    }
}
