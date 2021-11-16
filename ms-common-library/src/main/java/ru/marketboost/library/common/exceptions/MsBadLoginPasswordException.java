package ru.marketboost.library.common.exceptions;

public class MsBadLoginPasswordException extends MicroServiceException{

    public MsBadLoginPasswordException() {
        super("неверный логин или пароль");
    }

}
