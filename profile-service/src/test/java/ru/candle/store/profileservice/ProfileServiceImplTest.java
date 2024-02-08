package ru.candle.store.profileservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.profileservice.dictionary.ExceptionCode;
import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;
import ru.candle.store.profileservice.entity.ProfileEntity;
import ru.candle.store.profileservice.repository.ProfileRepository;
import ru.candle.store.profileservice.service.Impl.ProfileServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {

    @Mock
    ProfileRepository repository;

    @InjectMocks
    ProfileServiceImpl service;

    @Test
    void whenGetProfileNotFound() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(null));
        GetProfileResponse expResponse = GetProfileResponse.builder()
                .success(true)
                .build();

        GetProfileResponse response = service.getUserProfile(1L);
        Assertions.assertEquals(expResponse, response);
        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void whenGetProfileFail() {
        Mockito.when(repository.findById(1L)).thenThrow(IllegalArgumentException.class);
        GetProfileResponse expResponse = GetProfileResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                .build();

        GetProfileResponse response = service.getUserProfile(1L);
        Assertions.assertEquals(expResponse, response);
        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void whenGetProfileFound() {
        ProfileEntity profile = new ProfileEntity(1L, "First", "Last", "Middle", "City", "1990-01-01", "Address");
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(profile));
        GetProfileResponse expResponse = GetProfileResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .middleName("Middle")
                .city("City")
                .birthday("1990-01-01")
                .address("Address")
                .build();

        GetProfileResponse response = service.getUserProfile(1L);
        Assertions.assertEquals(expResponse, response);
        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void whenSaveProfileSuccess() {
        ProfileEntity profile = new ProfileEntity(1L, "First", "Last", "Middle", "City", "1990-01-01", "Address");
        Mockito.when(repository.save(profile)).thenReturn(profile);
        SaveProfileRequest request = new SaveProfileRequest("First", "Last", "Middle", "City", "1990-01-01", "Address");
        SaveProfileResponse expResponse = SaveProfileResponse.builder()
                .success(true)
                .build();

        SaveProfileResponse response = service.saveUserProfile(request, 1L);
        Assertions.assertEquals(expResponse, response);
        Mockito.verify(repository, Mockito.times(1)).save(profile);
    }

    @Test
    void whenSaveProfileFail() {
        ProfileEntity profile = new ProfileEntity(1L, "First", "Last", "Middle", "City", "1990-01-01", "Address");
        Mockito.when(repository.save(profile)).thenThrow(IllegalArgumentException.class);
        SaveProfileRequest request = new SaveProfileRequest("First", "Last", "Middle", "City", "1990-01-01", "Address");
        SaveProfileResponse expResponse = SaveProfileResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                .build();

        SaveProfileResponse response = service.saveUserProfile(request, 1L);
        Assertions.assertEquals(expResponse, response);
        Mockito.verify(repository, Mockito.times(1)).save(profile);
    }
}
