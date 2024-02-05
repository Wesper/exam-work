package ru.candle.store.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.candle.store.orderservice.dto.request.promocodes.AddPromocodeRequest;
import ru.candle.store.orderservice.dto.request.promocodes.ChangePromocodeActualRequest;
import ru.candle.store.orderservice.dto.response.Promocode;
import ru.candle.store.orderservice.dto.response.promocodes.AddPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.ChangePromocodeActualResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetAllPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetPromocodeResponse;
import ru.candle.store.orderservice.entity.PromocodeEntity;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.IPromocodeService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromocodeServiceImpl implements IPromocodeService {

    @Autowired
    private PromocodeRepository promocodeRepository;

    @Override
    public AddPromocodeResponse addPromocode(AddPromocodeRequest rq) {
        return addPromocodeResponse(rq);
    }

    @Override
    public GetPromocodeResponse getPromocode(String promocode) {
        return getPromocode(promocode);
    }

    @Override
    public ChangePromocodeActualResponse changePromocodeActual(ChangePromocodeActualRequest rq) {
        return changePromocodeActualResponse(rq);
    }

    @Override
    public GetAllPromocodeResponse getAllPromocodes() {
        return getAllPromocodesResponse();
    }

    private AddPromocodeResponse addPromocodeResponse(AddPromocodeRequest rq) {
        PromocodeEntity promocodeEntity = new PromocodeEntity(rq.getPromocode(), rq.getPercent(), rq.isActual());
        promocodeRepository.save(promocodeEntity);
        return new AddPromocodeResponse(true);
    }

    private GetPromocodeResponse getPromocodeResponse(String promocode) {
        PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(promocode);
        if (promocodeEntity == null) {
            throw new RuntimeException("Промокода не существует");
        }
        return new GetPromocodeResponse(promocodeEntity.getPromocode(), promocodeEntity.getPercent());
    }

    private ChangePromocodeActualResponse changePromocodeActualResponse(ChangePromocodeActualRequest rq) {
        PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(rq.getPromocode());
        if (promocodeEntity == null) {
            throw new RuntimeException("Промокода не существует");
        }
        promocodeEntity.setActual(rq.isActual());
        promocodeRepository.save(promocodeEntity);
        return new ChangePromocodeActualResponse(true);
    }

    private GetAllPromocodeResponse getAllPromocodesResponse() {
        List<PromocodeEntity> promocodeEntities = promocodeRepository.findAll();
        if (promocodeEntities.isEmpty()) {
            throw new RuntimeException("Промокодов нет");
        }
        List<Promocode> promocodes = new ArrayList<>();
        promocodeEntities.forEach(p -> {
            promocodes.add(new Promocode(
                    p.getPromocode(),
                    p.getPercent(),
                    p.getActual()
            ));
        });
        return new GetAllPromocodeResponse(promocodes);
    }
}
