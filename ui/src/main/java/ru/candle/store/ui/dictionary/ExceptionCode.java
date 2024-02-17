package ru.candle.store.ui.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION","Произошла непредвиденная ошибка"),
    AUTHORIZATION_HEADER_EMPTY("AUTHORIZATION_HEADER_EMPTY","В запросе отсутствует заголовок авторизации"),
    STATIC_ANSWER_IS_NULL("STATIC_ANSWER_IS_NULL", "Сервис статики вернул ответ null");

    private String errorCode;
    private String errorText;

}