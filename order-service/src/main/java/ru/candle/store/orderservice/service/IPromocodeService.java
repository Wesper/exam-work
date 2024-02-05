package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.request.promocodes.AddPromocodeRequest;
import ru.candle.store.orderservice.dto.request.promocodes.ChangePromocodeActualRequest;
import ru.candle.store.orderservice.dto.response.promocodes.AddPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.ChangePromocodeActualResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetAllPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetPromocodeResponse;

public interface IPromocodeService {

    AddPromocodeResponse addPromocode (AddPromocodeRequest rq);
    GetPromocodeResponse getPromocode (String promocode);
    ChangePromocodeActualResponse changePromocodeActual (ChangePromocodeActualRequest rq);
    GetAllPromocodeResponse getAllPromocodes();

}
