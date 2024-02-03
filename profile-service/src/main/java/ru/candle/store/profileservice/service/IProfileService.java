package ru.candle.store.profileservice.service;

import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;

public interface IProfileService {

    GetProfileResponse getUserProfile(Long userId);
    SaveProfileResponse saveUserProfile(SaveProfileRequest rq, Long userId);
}
