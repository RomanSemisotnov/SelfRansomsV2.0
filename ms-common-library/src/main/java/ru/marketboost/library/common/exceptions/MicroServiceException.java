package ru.marketboost.library.common.exceptions;

public class MicroServiceException extends Exception {
    public MicroServiceException(Throwable throwable) {
        super(throwable);
    }

    public MicroServiceException(String message) {
        super(message);

    }

    public MicroServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
