package ru.candle.store.ui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import ru.candle.store.ui.dto.request.AddRatingRequest;
import ru.candle.store.ui.dto.request.AddReviewRequest;

public interface IMainService {

    String getHome(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String getCard(String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String addReview(AddReviewRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String addRating(AddRatingRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
}
