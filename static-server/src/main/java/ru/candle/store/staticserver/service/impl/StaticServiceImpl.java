package ru.candle.store.staticserver.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.candle.store.staticserver.dictionary.ExceptionCode;
import ru.candle.store.staticserver.dto.Image;
import ru.candle.store.staticserver.dto.request.DeleteImageRequest;
import ru.candle.store.staticserver.dto.response.DeleteImageResponse;
import ru.candle.store.staticserver.dto.response.GetImagesListResponse;
import ru.candle.store.staticserver.dto.response.UploadImageResponse;
import ru.candle.store.staticserver.service.IStaticService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StaticServiceImpl implements IStaticService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.data.url}")
    private String minioUrl;

    @Override
    public GetImagesListResponse getAllImages() {
        try {
            return getAllImagesResponse();
        } catch (Exception e) {
            return GetImagesListResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public UploadImageResponse uploadImage(Image request) {
        try {
            return uploadImageResponse(request);
        } catch (Exception e) {
            return UploadImageResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public DeleteImageResponse deleteImage(DeleteImageRequest request) {
        try {
            return deleteImageResponse(request);
        } catch (Exception e) {
            return DeleteImageResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    private GetImagesListResponse getAllImagesResponse() throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        List<Image> images = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .recursive(true)
                .build());
        for (Result<Item> item : results) {
            images.add(Image.builder()
                    .filename(item.get().objectName())
                    .size(item.get().size())
                    .url(getPreSignedUrl(item.get().objectName()))
                    .build());
        }
        return GetImagesListResponse.builder()
                .success(true)
                .images(images)
                .build();
    }

    private String getPreSignedUrl(String imageName) {
        return minioUrl + "/" + bucketName + "/" + imageName;
    }


    private UploadImageResponse uploadImageResponse(Image request) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(request.getFilename())
                .contentType(request.getContentType())
                .stream(new ByteArrayInputStream(request.getFile()), request.getSize(), -1)
                .build());
        return UploadImageResponse.builder().success(true).build();
    }

    private DeleteImageResponse deleteImageResponse(DeleteImageRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(request.getImageName())
                .build());
        return DeleteImageResponse.builder().success(true).build();
    }
}
