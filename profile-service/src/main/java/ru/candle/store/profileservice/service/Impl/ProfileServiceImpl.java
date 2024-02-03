package ru.candle.store.profileservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;
import ru.candle.store.profileservice.entity.ProfileEntity;
import ru.candle.store.profileservice.repository.ProfileRepository;
import ru.candle.store.profileservice.service.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {

    @Autowired
    ProfileRepository repository;

    @Override
    public GetProfileResponse getUserProfile(Long userId) {
        return getProfile(userId);
    }

    @Override
    public SaveProfileResponse saveUserProfile(SaveProfileRequest rq, Long userId) {
        return saveProfile(rq, userId);
    }

    private GetProfileResponse getProfile(Long userId) {
        ProfileEntity entity = repository.findById(userId).orElseThrow(() -> new RuntimeException("Профиль не найден"));
        return GetProfileResponse.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .city(entity.getCity())
                .birthday(entity.getBirthday())
                .address(entity.getAddress())
                .build();
    }

    private SaveProfileResponse saveProfile(SaveProfileRequest rq, Long userId) {
        ProfileEntity entity = new ProfileEntity(
                userId,
                rq.getFirstName(),
                rq.getLastName(),
                rq.getMiddleName(),
                rq.getCity(),
                rq.getBirthday(),
                rq.getAddress()
                );
        repository.save(entity);
        return new SaveProfileResponse(true);
    }
}
