package ru.candle.store.profileservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;
import ru.candle.store.profileservice.entity.ProfileEntity;
import ru.candle.store.profileservice.repository.ProfileRepository;
import ru.candle.store.profileservice.service.IProfileService;
import ru.candle.store.profileservice.service.Impl.ProfileServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {

    @Mock
    ProfileRepository repository;

    @InjectMocks
    ProfileServiceImpl service;

    @Test
    void WhenGetProfileNotFound() {
        Mockito.when(repository.findById(1L)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> service.getUserProfile(1L));
        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void WhenGetProfileFound() {
        ProfileEntity profile = new ProfileEntity(1L, "First", "Last", "Middle", "City", "1990-01-01", "Address");
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(profile));
        GetProfileResponse expResponse = new GetProfileResponse("First", "Last", "Middle", "City", "1990-01-01", "Address");

        GetProfileResponse response = service.getUserProfile(1L);
        Assertions.assertEquals(expResponse, response);
        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void WhenSaveProfileSuccess() {
        ProfileEntity profile = new ProfileEntity(1L, "First", "Last", "Middle", "City", "1990-01-01", "Address");
        Mockito.when(repository.save(profile)).thenReturn(profile);
        SaveProfileRequest request = new SaveProfileRequest("First", "Last", "Middle", "City", "1990-01-01", "Address");
        SaveProfileResponse expResponse = new SaveProfileResponse(true);

        SaveProfileResponse response = service.saveUserProfile(request, 1L);
        Assertions.assertEquals(expResponse, response);
        Mockito.verify(repository, Mockito.times(1)).save(profile);
    }

    @Test
    void WhenSaveProfileFail() {
        ProfileEntity profile = new ProfileEntity(1L, "First", "Last", "Middle", "City", "1990-01-01", "Address");
        Mockito.when(repository.save(profile)).thenThrow(RuntimeException.class);
        SaveProfileRequest request = new SaveProfileRequest("First", "Last", "Middle", "City", "1990-01-01", "Address");

        Assertions.assertThrows(RuntimeException.class, () -> service.saveUserProfile(request, 1L));
        Mockito.verify(repository, Mockito.times(1)).save(profile);
    }
}
