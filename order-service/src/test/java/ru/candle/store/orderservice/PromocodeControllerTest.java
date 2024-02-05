package ru.candle.store.orderservice;

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
import ru.candle.store.orderservice.config.OrderConfig;
import ru.candle.store.orderservice.controller.PromocodeController;
import ru.candle.store.orderservice.dto.request.promocodes.AddPromocodeRequest;
import ru.candle.store.orderservice.dto.request.promocodes.ChangePromocodeActualRequest;
import ru.candle.store.orderservice.dto.response.Promocode;
import ru.candle.store.orderservice.dto.response.promocodes.AddPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.ChangePromocodeActualResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetAllPromocodesResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetPromocodeResponse;
import ru.candle.store.orderservice.service.impl.PromocodeServiceImpl;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PromocodeController.class)
@AutoConfigureMockMvc()
@Import(OrderConfig.class)
public class PromocodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromocodeServiceImpl service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenAddPromocodeSuccess() throws Exception {
        AddPromocodeRequest rq = new AddPromocodeRequest("promo", 2L, true);
        Mockito.when(service.addPromocode(rq)).thenReturn(new AddPromocodeResponse(true));
        AddPromocodeResponse rs = new AddPromocodeResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/promocode/add").header("role", "ADMIN").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenAddPromocodeFail() throws Exception {
        AddPromocodeRequest rq = new AddPromocodeRequest("promo", 2L, true);
        Mockito.when(service.addPromocode(rq)).thenReturn(new AddPromocodeResponse(true));
        AddPromocodeResponse rs = new AddPromocodeResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/promocode/add").header("role", "MANAGER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenGetPromocodeSuccess() throws Exception {
        GetPromocodeResponse rs = new GetPromocodeResponse("promo", 1L);
        Mockito.when(service.getPromocode("promo")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/promocode/get/promo").header("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetPromocodeFail() throws Exception {
        GetPromocodeResponse rs = new GetPromocodeResponse("promo", 1L);
        Mockito.when(service.getPromocode("promo")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/promocode/get/promo").header("role", "MANAGER"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenGetAllPromocodesSuccess() throws Exception {
        List<Promocode> promocodes = List.of(
                new Promocode("promo", 1L, true),
                new Promocode("code", 2L, false)
        );
        GetAllPromocodesResponse rs = new GetAllPromocodesResponse(promocodes);
        Mockito.when(service.getAllPromocodes()).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/promocode/get").header("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetAllPromocodesFail() throws Exception {
        List<Promocode> promocodes = List.of(
                new Promocode("promo", 1L, true),
                new Promocode("code", 2L, false)
        );
        GetAllPromocodesResponse rs = new GetAllPromocodesResponse(promocodes);
        Mockito.when(service.getAllPromocodes()).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/promocode/get").header("role", "User"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenChangePromocodeActualSuccess() throws Exception {
        ChangePromocodeActualRequest rq = new ChangePromocodeActualRequest("promo", false);
        Mockito.when(service.changePromocodeActual(rq)).thenReturn(new ChangePromocodeActualResponse(true));
        AddPromocodeResponse rs = new AddPromocodeResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/promocode/actual/change").header("role", "ADMIN").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenChangePromocodeActualFail() throws Exception {
        ChangePromocodeActualRequest rq = new ChangePromocodeActualRequest("promo", false);
        Mockito.when(service.changePromocodeActual(rq)).thenReturn(new ChangePromocodeActualResponse(true));
        AddPromocodeResponse rs = new AddPromocodeResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/promocode/actual/change").header("role", "MANAGER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
