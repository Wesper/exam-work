package ru.candle.store.profileservice.service;

import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;

public interface IProfileService {

    /**
     * Получение профиля пользователя
     * @param userId идентификатор пользователя
     * @return профиль пользователя
     */
    GetProfileResponse getUserProfile(Long userId);
    /**
     * Сохранение профиля пользователя
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @return true или false
     */
    SaveProfileResponse saveUserProfile(SaveProfileRequest rq, Long userId);
}
