package ru.candle.store.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.candle.store.ui.dto.request.SaveProfileRequest;
import ru.candle.store.ui.service.IProfileService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private IProfileService service;

    @GetMapping()
    public String showProfile(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.showProfile(model, servletRequest, servletResponse);
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute SaveProfileRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return service.saveProfile(request, model, servletRequest, servletResponse);
    }
}
