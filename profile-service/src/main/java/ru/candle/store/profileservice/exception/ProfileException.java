package ru.candle.store.profileservice.exception;

import lombok.Getter;
import ru.candle.store.profileservice.dictionary.ExceptionCode;

@Getter
public class ProfileException extends Exception {

    private ExceptionCode e;

    public ProfileException() {
        super();
    }

    public ProfileException(String message) {
        super(message);
    }

    public ProfileException(ExceptionCode e, String message) {
        super(message);
        this.e = e;

    }
    public ProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileException(Throwable cause) {
        super(cause);
    }
}
