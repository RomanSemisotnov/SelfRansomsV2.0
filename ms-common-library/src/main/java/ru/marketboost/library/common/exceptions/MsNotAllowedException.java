package ru.marketboost.library.common.exceptions;

public class MsNotAllowedException extends MicroServiceException {

    public MsNotAllowedException() {
        super("Нет доступа");
    }

    public MsNotAllowedException(String message) {
        super(message);
    }

}
