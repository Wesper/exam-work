package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.request.promocodes.AddPromocodeRequest;
import ru.candle.store.orderservice.dto.request.promocodes.ChangePromocodeActualRequest;
import ru.candle.store.orderservice.dto.response.promocodes.AddPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.ChangePromocodeActualResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetAllPromocodesResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetPromocodeResponse;

public interface IPromocodeService {

    /**
     * Добавление промокода
     * @param rq запрос
     * @return true или false
     */
    AddPromocodeResponse addPromocode (AddPromocodeRequest rq);

    /**
     * Получение информации о промокоде
     * @param promocode промокод
     * @return информация о промокоде
     */
    GetPromocodeResponse getPromocode (String promocode);

    /**
     * Изменение статуса промокода
     * @param rq запрос
     * @return true или false
     */
    ChangePromocodeActualResponse changePromocodeActual (ChangePromocodeActualRequest rq);

    /**
     * Получение списка промокодов
     * @return список промокодов
     */
    GetAllPromocodesResponse getAllPromocodes();

}
