package ru.candle.store.ui.exception;

import ru.candle.store.ui.dictionary.ExceptionCode;

public class UIException extends Exception {

    private ExceptionCode e;

    public UIException() {
        super();
    }

    public UIException(String message) {
        super(message);
    }

    public UIException(ExceptionCode e, String message) {
        super(message);
        this.e = e;

    }
    public UIException(String message, Throwable cause) {
        super(message, cause);
    }

    public UIException(Throwable cause) {
        super(cause);
    }
}
