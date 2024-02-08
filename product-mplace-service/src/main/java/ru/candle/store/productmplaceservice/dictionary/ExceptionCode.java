package ru.candle.store.productmplaceservice.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    NOT_FOUND("NOT_FOUND","Продукт не найден"),
    NOT_FOUND_ALL("NOT_FOUND_ALL","Продукты не найдены"),
    NOT_FOUND_BY_IDS("NOT_FOUND_BY_IDS","Продукты по искомым идентификаторам не найдены"),
    CHECK_PURCHASED_FAIL("NOT_FOUND_BY_IDS","Продукты по искомым идентификаторам не найдены"),
    UPDATE_AVAILABLE_FAIL("UPDATE_AVAILABLE_FAIL","Не удалось обновить доступность продукта"),
    REVIEW_ACCESS_DENY("REVIEW_ACCESS_DENY", "Нельзя оставить отзыв на не приобретенный продукт"),
    RATING_ACCESS_DENY("RATING_ACCESS_DENY", "Нельзя оценить не приобретенный продукт"),
    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION","Произошла непредвиденная ошибка");

    private String errorCode;
    private String errorText;
}
