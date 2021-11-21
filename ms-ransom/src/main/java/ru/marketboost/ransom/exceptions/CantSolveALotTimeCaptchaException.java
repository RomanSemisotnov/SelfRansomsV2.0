package ru.marketboost.ransom.exceptions;

import java.util.UUID;

public class CantSolveALotTimeCaptchaException extends Exception {

    public CantSolveALotTimeCaptchaException(UUID sessionId, int retry) {
        super("Cant solve " + retry + " time captcha, sessionId: " + sessionId);
    }

}
