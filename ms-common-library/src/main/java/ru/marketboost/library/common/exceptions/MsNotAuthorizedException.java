package ru.marketboost.library.common.exceptions;

public class MsNotAuthorizedException extends MicroServiceException{

    public MsNotAuthorizedException() {
        super("Не авторизован");
    }
    public MsNotAuthorizedException(String text) {
        super(text);
    }

}
