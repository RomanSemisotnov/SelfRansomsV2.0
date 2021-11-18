package ru.marketboost.ransom.exceptions;

public class CouldntGetRightPhoneCodeException extends Exception{

    public CouldntGetRightPhoneCodeException(Throwable throwable) {
        super(throwable);
    }

    public CouldntGetRightPhoneCodeException(String message) {
        super(message);

    }

    public CouldntGetRightPhoneCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
