package ru.candle.store.orderservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
import ru.candle.store.orderservice.dto.request.promocodes.AddPromocodeRequest;
import ru.candle.store.orderservice.dto.request.promocodes.ChangePromocodeActualRequest;
import ru.candle.store.orderservice.dto.response.Promocode;
import ru.candle.store.orderservice.dto.response.promocodes.AddPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.ChangePromocodeActualResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetAllPromocodesResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetPromocodeResponse;
import ru.candle.store.orderservice.entity.PromocodeEntity;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.impl.PromocodeServiceImpl;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PromocodeServiceTest {

    @Mock
    private PromocodeRepository promocodeRepository;

    @InjectMocks
    private PromocodeServiceImpl service;

    @Test
    void whenAddPromocodeSuccess() {
        AddPromocodeRequest rq = new AddPromocodeRequest("promo", 10L, true);
        PromocodeEntity promocodeEntity = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.save(promocodeEntity)).thenReturn(promocodeEntity);

        AddPromocodeResponse rs = service.addPromocode(rq);
        Assertions.assertEquals(AddPromocodeResponse.builder().success(true).build(), rs);
    }

    @Test
    void whenAddPromocodeFalse() {
        AddPromocodeRequest rq = new AddPromocodeRequest("promo", 10L, true);
        PromocodeEntity promocodeEntity = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.save(promocodeEntity)).thenThrow(IllegalArgumentException.class);

        AddPromocodeResponse rs = service.addPromocode(rq);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenGetPromocodeSuccess() {
        String promocode = "promo";
        PromocodeEntity promocodeEntity = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode(promocode)).thenReturn(promocodeEntity);
        GetPromocodeResponse expRs = GetPromocodeResponse.builder()
                .success(true)
                .promocode("promo")
                .percent(10L)
                .build();
        GetPromocodeResponse actRs = service.getPromocode(promocode);
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetPromocodeFalse() {
        String promocode = "promo";
        Mockito.when(promocodeRepository.findByPromocode(promocode)).thenReturn(null);

        GetPromocodeResponse rs = service.getPromocode(promocode);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenChangePromocodeActualSuccess() {
        ChangePromocodeActualRequest rq = new ChangePromocodeActualRequest("promo", false);
        PromocodeEntity promocodeEntityBefore = new PromocodeEntity("promo", 10L, true);
        PromocodeEntity promocodeEntityAfter = new PromocodeEntity("promo", 10L, false);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocodeEntityBefore);
        Mockito.when(promocodeRepository.save(promocodeEntityAfter)).thenReturn(promocodeEntityAfter);
        ChangePromocodeActualResponse actRs = service.changePromocodeActual(rq);
        Assertions.assertEquals(ChangePromocodeActualResponse.builder().success(true).build(), actRs);
    }

    @Test
    void whenChangePromocodeActualFalse() {
        ChangePromocodeActualRequest rq = new ChangePromocodeActualRequest("promo", false);
        Mockito.when(promocodeRepository.findByPromocode(rq.getPromocode())).thenReturn(null);

        ChangePromocodeActualResponse rs = service.changePromocodeActual(rq);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenGetAllPromocodesResponseSuccess() {
        List<PromocodeEntity> promocodesEntity = List.of(
                new PromocodeEntity("promo", 10L, false),
                new PromocodeEntity("code", 20L, true));
        List<Promocode> promocodes = List.of(
                new Promocode("promo", 10L, false),
                new Promocode("code", 20L, true)
        );
        GetAllPromocodesResponse expRs = GetAllPromocodesResponse.builder()
                .success(true)
                .promocodes(promocodes)
                .build();

        Mockito.when(promocodeRepository.findAll()).thenReturn(promocodesEntity);
        GetAllPromocodesResponse actRs = service.getAllPromocodes();
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetAllPromocodesResponseEmpty() {
        Mockito.when(promocodeRepository.findAll()).thenReturn(new ArrayList<>());

        GetAllPromocodesResponse rs = service.getAllPromocodes();
        Assertions.assertAll(
                () -> Assertions.assertTrue(rs.getSuccess()),
                () -> Assertions.assertEquals(new ArrayList<>(), rs.getPromocodes())
        );
    }
}
