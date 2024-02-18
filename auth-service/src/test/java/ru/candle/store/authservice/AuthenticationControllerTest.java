package ru.candle.store.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.candle.store.authservice.config.JwtAuthenticationFilter;
import ru.candle.store.authservice.controller.AuthenticationController;
import ru.candle.store.authservice.dto.response.GetTokenInfoResponse;
import ru.candle.store.authservice.dto.response.JwtAuthenticationResponse;
import ru.candle.store.authservice.dto.response.ValidateResponse;
import ru.candle.store.authservice.service.AuthenticationService;
import ru.candle.store.authservice.service.UserManagementService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserManagementService userManagementService;

    @MockBean
    private JwtAuthenticationFilter filter;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenSignUpSuccess() throws Exception {
        String requset = "{\"username\": \"one\", \"password\":  \"onepass\", \"email\": \"one@one.ru\"}";
        Mockito.when(authenticationService.signUp(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signUp").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"token\"}"));
    }

    @Test
    void whenSignUpFail() throws Exception {
        String requset = "{\"username\": \"one\", \"password\":  \"onepass\"}";
        Mockito.when(authenticationService.signUp(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signUp").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenSignInSuccess() throws Exception {
        String requset = "{\"username\": \"one\", \"password\":  \"onepass\"}";
        Mockito.when(authenticationService.signIn(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signIn").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"token\"}"));
    }

    @Test
    void whenSignInFail() throws Exception {
        String requset = "{\"username\": \"one\"}";
        Mockito.when(authenticationService.signIn(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signIn").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenValidateSuccess() throws Exception {
        String requset = "{\"token\": \"token\"}";
        Mockito.when(authenticationService.isValid(Mockito.any())).thenReturn(new ValidateResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/validate").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": true}"));
    }

    @Test
    void whenValidateFail() throws Exception {
        String requset = "{\"token\": \"\"}";
        Mockito.when(authenticationService.isValid(Mockito.any())).thenReturn(new ValidateResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/validate").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenGetTokenInfoSuccess() throws Exception {
        String requset = "{\"token\": \"token\"}";
        Mockito.when(authenticationService.getTokenInfo(Mockito.any())).thenReturn(new GetTokenInfoResponse(1L, "one", "one@one.ru", "ROLE_USER"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token/info/get").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userId\": 1, \"username\": \"one\", \"email\": \"one@one.ru\", \"role\": \"ROLE_USER\"}"));
    }

    @Test
    void whenGetTokenInfoFail() throws Exception {
        String requset = "{\"token\": \"\"}";
        Mockito.when(authenticationService.getTokenInfo(Mockito.any())).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token/info/get").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}