package ru.marketboost.library.common.exceptions;

public class MsInternalErrorException extends MicroServiceException{

    public MsInternalErrorException(String message) {
        super(message);
    }

    public MsInternalErrorException(String message, Exception e) {
        super(message, e);
    }

}
