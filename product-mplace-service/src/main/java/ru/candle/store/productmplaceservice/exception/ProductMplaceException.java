package ru.candle.store.productmplaceservice.exception;

import lombok.Getter;
import ru.candle.store.productmplaceservice.dictionary.ExceptionCode;

@Getter
public class ProductMplaceException extends Exception {

    private ExceptionCode e;

    public ProductMplaceException() {
        super();
    }

    public ProductMplaceException(String message) {
        super(message);
    }

    public ProductMplaceException(ExceptionCode e, String message) {
        super(message);
        this.e = e;

    }
    public ProductMplaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductMplaceException(Throwable cause) {
        super(cause);
    }
}
