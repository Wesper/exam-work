package ru.candle.store.staticserver.exception;

import lombok.Getter;
import ru.candle.store.staticserver.dictionary.ExceptionCode;

@Getter
public class StaticException extends Exception {

    private ExceptionCode e;

    public StaticException() {
        super();
    }

    public StaticException(String message) {
        super(message);
    }

    public StaticException(ExceptionCode e, String message) {
        super(message);
        this.e = e;

    }
    public StaticException(String message, Throwable cause) {
        super(message, cause);
    }

    public StaticException(Throwable cause) {
        super(cause);
    }
}