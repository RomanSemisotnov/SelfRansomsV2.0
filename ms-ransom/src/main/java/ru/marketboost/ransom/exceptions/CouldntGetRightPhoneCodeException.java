package ru.marketboost.ransom.exceptions;

import java.util.UUID;

public class CouldntGetRightPhoneCodeException extends Exception{


    public CouldntGetRightPhoneCodeException(UUID id, int count) {
        super(String.format("Cant get not expired phone code on login page by %s times, sessionId: %s", count, id));
    }

}
