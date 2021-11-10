package ru.marketboost.ransom.exceptions;

public class OneOfTwoElementNotFoundException extends Exception {

    public OneOfTwoElementNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public OneOfTwoElementNotFoundException(String message) {
        super(message);

    }

    public OneOfTwoElementNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
