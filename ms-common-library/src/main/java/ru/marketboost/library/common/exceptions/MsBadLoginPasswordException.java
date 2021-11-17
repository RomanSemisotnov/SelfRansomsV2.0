package ru.marketboost.library.common.exceptions;

public class MsBadLoginPasswordException extends MicroServiceException{

    public MsBadLoginPasswordException() {
        super("Неверный логин или пароль");
    }

}
