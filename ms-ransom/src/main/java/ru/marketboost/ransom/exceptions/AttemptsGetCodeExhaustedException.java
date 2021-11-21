package ru.marketboost.ransom.exceptions;

import java.util.UUID;

public class AttemptsGetCodeExhaustedException extends Exception {

    public AttemptsGetCodeExhaustedException(UUID uuid) {
        super(String.format(
                "Attempt to login was exhausted, session id: %s", uuid
        ));
    }

    public AttemptsGetCodeExhaustedException(String message) {
        super(message);
    }

}
