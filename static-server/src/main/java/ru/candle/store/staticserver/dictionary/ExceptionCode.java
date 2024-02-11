package ru.candle.store.staticserver.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION","Произошла непредвиденная ошибка");

    private String errorCode;
    private String errorText;

}
