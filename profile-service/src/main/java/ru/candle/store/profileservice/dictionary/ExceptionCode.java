package ru.candle.store.profileservice.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    NOT_FOUND("NOT_FOUND","Профиль не найден"),
    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION","Произошла непредвиденная ошибка");

    private String errorCode;
    private String errorText;

}
