package ru.candle.store.staticserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.staticserver.dto.Image;
import ru.candle.store.staticserver.dto.request.DeleteImageRequest;
import ru.candle.store.staticserver.dto.response.DeleteImageResponse;
import ru.candle.store.staticserver.dto.response.GetImagesListResponse;
import ru.candle.store.staticserver.dto.response.UploadImageResponse;
import ru.candle.store.staticserver.service.IStaticService;

@RestController
@RequestMapping("/images")
public class StaticController {

    @Autowired
    private IStaticService service;

    /**
     * Получение списка изображений в хранилище
     * @return список изображений
     */
    @PreAuthorize("#role == 'ADMIN'")
    @GetMapping()
    public GetImagesListResponse getAllImages(@RequestHeader("role") String role) {
        return service.getAllImages();
    }

    /**
     * Загрузка изображения в хранилище
     * @param request запрос с данными изображения
     * @return признак успеха
     */
    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/upload")
    public UploadImageResponse uploadImage(@RequestHeader("role") String role, @ModelAttribute Image request) {
        return service.uploadImage(request);
    }

    /**
     * Удвление изображения из хранилища
     * @param request запрос с именем изображения
     * @return признак успеха
     */
    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/delete")
    public DeleteImageResponse deleteImage(@RequestHeader("role") String role, @RequestBody DeleteImageRequest request) {
        return service.deleteImage(request);
    }

}
