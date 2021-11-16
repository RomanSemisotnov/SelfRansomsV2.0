package ru.marketboost.library.common.exceptions;

public class MsBadRequestException extends MicroServiceException {

    public MsBadRequestException(String parametername, Object parameterValue) {
        super(String.format("Неверный аргумент. Название: %s. Значение: %s", parametername, parameterValue));
    }

    public MsBadRequestException(String message) {
        super(message);
    }

}
