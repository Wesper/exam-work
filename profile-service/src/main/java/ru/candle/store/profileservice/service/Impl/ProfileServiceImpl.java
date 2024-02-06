package ru.candle.store.profileservice.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.candle.store.profileservice.dictionary.ExceptionCode;
import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;
import ru.candle.store.profileservice.entity.ProfileEntity;
import ru.candle.store.profileservice.exception.ProfileException;
import ru.candle.store.profileservice.repository.ProfileRepository;
import ru.candle.store.profileservice.service.IProfileService;

@Slf4j
@Service
public class ProfileServiceImpl implements IProfileService {

    @Autowired
    ProfileRepository repository;

    @Override
    public GetProfileResponse getUserProfile(Long userId) {
        try {
            return getProfile(userId);
        } catch (ProfileException e) {
            log.warn(e.getMessage());
            return GetProfileResponse.builder().success(true).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GetProfileResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public SaveProfileResponse saveUserProfile(SaveProfileRequest rq, Long userId) {
        try {
            return saveProfile(rq, userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return SaveProfileResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    private GetProfileResponse getProfile(Long userId) throws ProfileException {
        ProfileEntity entity = repository.findById(userId).orElseThrow(() -> new ProfileException(ExceptionCode.NOT_FOUND, "Профиль не найден"));
        return GetProfileResponse.builder()
                .success(true)
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
        return SaveProfileResponse.builder().success(true).build();
    }
}
