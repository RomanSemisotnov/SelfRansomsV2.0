package ru.marketboost.ransom.exceptions;

public class UnknownSituationException extends Exception {

    public UnknownSituationException(Throwable throwable) {
        super(throwable);
    }

    public UnknownSituationException(String message) {
        super(message);

    }

    public UnknownSituationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
