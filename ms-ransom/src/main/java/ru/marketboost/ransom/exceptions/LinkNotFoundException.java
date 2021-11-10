package ru.marketboost.ransom.exceptions;

public class LinkNotFoundException extends Exception {

    public LinkNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public LinkNotFoundException(String message) {
        super(message);

    }

    public LinkNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
