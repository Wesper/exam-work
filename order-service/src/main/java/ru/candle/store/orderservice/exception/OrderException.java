package ru.candle.store.orderservice.exception;

import lombok.Getter;
import ru.candle.store.orderservice.dictionary.ExceptionCode;

@Getter
public class OrderException extends Exception {

    private ExceptionCode e;

    public OrderException() {
        super();
    }

    public OrderException(String message) {
        super(message);
    }

    public OrderException(ExceptionCode e, String message) {
        super(message);
        this.e = e;

    }
    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderException(Throwable cause) {
        super(cause);
    }
}
