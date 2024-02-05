package ru.candle.store.orderservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
        Assertions.assertEquals(new AddPromocodeResponse(true), rs);
    }

    @Test
    void whenAddPromocodeFalse() {
        AddPromocodeRequest rq = new AddPromocodeRequest("promo", 10L, true);
        PromocodeEntity promocodeEntity = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.save(promocodeEntity)).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(RuntimeException.class, () -> service.addPromocode(rq));
    }

    @Test
    void whenGetPromocodeSuccess() {
        String promocode = "promo";
        PromocodeEntity promocodeEntity = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode(promocode)).thenReturn(promocodeEntity);
        GetPromocodeResponse expRs = new GetPromocodeResponse("promo", 10L);
        GetPromocodeResponse actRs = service.getPromocode(promocode);
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetPromocodeFalse() {
        String promocode = "promo";
        Mockito.when(promocodeRepository.findByPromocode(promocode)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> service.getPromocode(promocode));
    }

    @Test
    void whenChangePromocodeActualSuccess() {
        ChangePromocodeActualRequest rq = new ChangePromocodeActualRequest("promo", false);
        PromocodeEntity promocodeEntityBefore = new PromocodeEntity("promo", 10L, true);
        PromocodeEntity promocodeEntityAfter = new PromocodeEntity("promo", 10L, false);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocodeEntityBefore);
        Mockito.when(promocodeRepository.save(promocodeEntityAfter)).thenReturn(promocodeEntityAfter);
        ChangePromocodeActualResponse actRs = service.changePromocodeActual(rq);
        Assertions.assertEquals(new ChangePromocodeActualResponse(true), actRs);
    }

    @Test
    void whenChangePromocodeActualFalse() {
        ChangePromocodeActualRequest rq = new ChangePromocodeActualRequest("promo", false);
        Mockito.when(promocodeRepository.findByPromocode(rq.getPromocode())).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> service.changePromocodeActual(rq));
    }

    @Test
    void whenGetAllPromocodesResponseSuccess() {
        List<PromocodeEntity> promocodesEntity = new ArrayList<>();
        promocodesEntity.add(new PromocodeEntity("promo", 10L, false));
        promocodesEntity.add(new PromocodeEntity("code", 20L, true));
        GetAllPromocodesResponse expRs = new GetAllPromocodesResponse();
        expRs.setPromocodes(new ArrayList<>());
        expRs.getPromocodes().add(new Promocode("promo", 10L, false));
        expRs.getPromocodes().add(new Promocode("code", 20L, true));

        Mockito.when(promocodeRepository.findAll()).thenReturn(promocodesEntity);
        GetAllPromocodesResponse actRs = service.getAllPromocodes();
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetAllPromocodesResponseFalse() {
        Mockito.when(promocodeRepository.findAll()).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> service.getAllPromocodes());
    }
}
