package ru.candle.store.ui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.candle.store.ui.dto.request.SaveProfileRequest;

public interface IProfileService {

    String showProfile(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
    String saveProfile(@ModelAttribute SaveProfileRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
}
