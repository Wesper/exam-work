package ru.candle.store.profileservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
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
import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;
import ru.candle.store.profileservice.service.IProfileService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void WhenRequestGetProfileSuccess() throws Exception {
        Long userId = 1L;
        GetProfileResponse response = new GetProfileResponse("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        Mockito.when(service.getUserProfile(userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "USER").header("userId", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestGetProfileWithoutHeadersFail() throws Exception {
        Long userId = 1L;
        GetProfileResponse response = new GetProfileResponse("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        Mockito.when(service.getUserProfile(userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "ADMIN").header("userId", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("userId", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "USER").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestGetProfileFail() throws Exception {
        Long userId = 1L;
        Mockito.when(service.getUserProfile(userId)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.get("/profile/get").header("role", "USER").header("userId", 1L).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void WhenRequestSaveProfileSuccess() throws Exception {
        Long userId = 1L;
        SaveProfileRequest rq = new SaveProfileRequest("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        SaveProfileResponse response = new SaveProfileResponse(true);
        Mockito.when(service.saveUserProfile(rq, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestSaveProfileWithoutHeadersFail() throws Exception {
        Long userId = 1L;
        SaveProfileRequest rq = new SaveProfileRequest("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        SaveProfileResponse response = new SaveProfileResponse(true);
        Mockito.when(service.saveUserProfile(rq, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("role", "ADMIN").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestSaveProfileFail() throws Exception {
        Long userId = 1L;
        SaveProfileRequest rq = new SaveProfileRequest("Firstname", "Lastname", "Middlename", "City", "1990-01-01", "Address");
        Mockito.when(service.saveUserProfile(rq, userId)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/profile/save").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON)));
    }
}
