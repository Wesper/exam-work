package ru.candle.store.profileservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.candle.store.profileservice.config.ProfileConfig;
import ru.candle.store.profileservice.controller.ProfileController;
import ru.candle.store.profileservice.dictionary.ExceptionCode;
import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;
import ru.candle.store.profileservice.service.IProfileService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class)
@AutoConfigureMockMvc()
@Import(ProfileConfig.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProfileService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenRequestGetProfileSuccess() throws Exception {
        Long userId = 1L;
        GetProfileResponse response = GetProfileResponse.builder()
                .success(true)
                .firstName("Firstname")
                .lastName("Lastname")
                .middleName("Middlename")
                .city("City")
                .birthday("1990-01-01")
                .address("Address")
                .build();

        Mockito.when(service.getUserProfile(userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "USER").header("userId", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestGetProfileWithoutHeadersFail() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("userId", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "USER").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenRequestGetProfileFail() throws Exception {
        Long userId = 1L;
        GetProfileResponse response = GetProfileResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                .build();
        Mockito.when(service.getUserProfile(userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "USER").header("userId", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestGetProfileEmptySuccess() throws Exception {
        Long userId = 1L;
        GetProfileResponse response = new GetProfileResponse();
        Mockito.when(service.getUserProfile(userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "USER").header("userId", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestSaveProfileSuccess() throws Exception {
        Long userId = 1L;
        SaveProfileRequest rq = new SaveProfileRequest("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        SaveProfileResponse response = SaveProfileResponse.builder()
                .success(true)
                .build();
        Mockito.when(service.saveUserProfile(rq, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestSaveProfileWithoutHeadersFail() throws Exception {
        Long userId = 1L;
        SaveProfileRequest rq = new SaveProfileRequest("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        SaveProfileResponse response = SaveProfileResponse.builder()
                .success(true)
                .build();
        Mockito.when(service.saveUserProfile(rq, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenRequestSaveProfileFail() throws Exception {
        Long userId = 1L;
        SaveProfileRequest rq = new SaveProfileRequest("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        SaveProfileResponse response = SaveProfileResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                .build();
        Mockito.when(service.saveUserProfile(rq, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
