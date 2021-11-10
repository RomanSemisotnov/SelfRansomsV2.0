package ru.marketboost.ransom.exceptions;

public class CantSolveALotTimeCaptchaException extends Exception {

    public CantSolveALotTimeCaptchaException(int retry) {
        super("Cant solve " + retry + " time captcha");
    }

}
