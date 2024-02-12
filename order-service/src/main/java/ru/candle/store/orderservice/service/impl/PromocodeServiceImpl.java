package ru.candle.store.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
import ru.candle.store.orderservice.dto.request.promocodes.AddPromocodeRequest;
import ru.candle.store.orderservice.dto.request.promocodes.ChangePromocodeActualRequest;
import ru.candle.store.orderservice.dto.response.Promocode;
import ru.candle.store.orderservice.dto.response.promocodes.AddPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.ChangePromocodeActualResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetAllPromocodesResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetPromocodeResponse;
import ru.candle.store.orderservice.entity.PromocodeEntity;
import ru.candle.store.orderservice.exception.OrderException;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.IPromocodeService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PromocodeServiceImpl implements IPromocodeService {

    @Autowired
    private PromocodeRepository promocodeRepository;

    @Override
    public AddPromocodeResponse addPromocode(AddPromocodeRequest rq) {
        try {
            return addPromocodeResponse(rq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AddPromocodeResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public GetPromocodeResponse getPromocode(String promocode) {
        try {
            return getPromocodeResponse(promocode);
        } catch (OrderException e) {
            log.error(e.getMessage(), e);
            return GetPromocodeResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return GetPromocodeResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public ChangePromocodeActualResponse changePromocodeActual(ChangePromocodeActualRequest rq) {
        try {
            return changePromocodeActualResponse(rq);
        } catch (OrderException e) {
            log.error(e.getMessage(), e);
            return ChangePromocodeActualResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ChangePromocodeActualResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public GetAllPromocodesResponse getAllPromocodes() {
        try {
            return getAllPromocodesResponse();
        } catch (OrderException e) {
            log.warn(e.getMessage());
            return GetAllPromocodesResponse.builder()
                    .success(true)
                    .promocodes(new ArrayList<>())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return GetAllPromocodesResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    private AddPromocodeResponse addPromocodeResponse(AddPromocodeRequest rq) {
        PromocodeEntity promocodeEntity = new PromocodeEntity(rq.getPromocode(), rq.getPercent(), rq.getActual());
        promocodeRepository.save(promocodeEntity);
        return AddPromocodeResponse.builder().success(true).build();
    }

    private GetPromocodeResponse getPromocodeResponse(String promocode) throws OrderException {
        PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(promocode);
        if (promocodeEntity == null) {
            throw new OrderException(ExceptionCode.PROMOCODE_NOT_FOUND, "Промокода не существует");
        }
        return GetPromocodeResponse.builder()
                .success(true)
                .promocode(promocodeEntity.getPromocode())
                .percent(promocodeEntity.getPercent())
                .build();
    }

    private ChangePromocodeActualResponse changePromocodeActualResponse(ChangePromocodeActualRequest rq) throws OrderException {
        PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(rq.getPromocode());
        if (promocodeEntity == null) {
            throw new OrderException(ExceptionCode.PROMOCODE_NOT_FOUND, "Промокода не существует");
        }
        promocodeEntity.setActual(rq.isActual());
        promocodeRepository.save(promocodeEntity);
        return ChangePromocodeActualResponse.builder().success(true).build();
    }

    private GetAllPromocodesResponse getAllPromocodesResponse() throws OrderException {
        List<PromocodeEntity> promocodeEntities = promocodeRepository.findAll();
        if (promocodeEntities.isEmpty()) {
            throw new OrderException(ExceptionCode.PROMOCODE_LIST_IS_EMPTY, "Промокодов нет");
        }
        List<Promocode> promocodes = new ArrayList<>();
        promocodeEntities.forEach(p -> {
            promocodes.add(new Promocode(
                    p.getPromocode(),
                    p.getPercent(),
                    p.getActual()
            ));
        });
        return GetAllPromocodesResponse.builder().success(true).promocodes(promocodes).build();
    }
}
