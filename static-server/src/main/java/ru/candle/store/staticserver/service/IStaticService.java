package ru.candle.store.staticserver.service;

import ru.candle.store.staticserver.dto.Image;
import ru.candle.store.staticserver.dto.request.DeleteImageRequest;
import ru.candle.store.staticserver.dto.response.DeleteImageResponse;
import ru.candle.store.staticserver.dto.response.GetImagesListResponse;
import ru.candle.store.staticserver.dto.response.UploadImageResponse;

public interface IStaticService {

    /**
     * Получение списка изображений в хранилище
     * @return список изображений
     */
    GetImagesListResponse getAllImages();

    /**
     * Загрузка изображения в хранилище
     * @param request запрос с данными изображения
     * @return признак успеха
     */
    UploadImageResponse uploadImage(Image request);

    /**
     * Удвление изображения из хранилища
     * @param request запрос с именем изображения
     * @return признак успеха
     */
    DeleteImageResponse deleteImage(DeleteImageRequest request);
}
