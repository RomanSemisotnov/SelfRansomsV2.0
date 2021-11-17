package ru.marketboost.library.common.exceptions;

import io.vavr.Tuple2;
import io.vavr.Tuple4;
import io.vavr.Tuple6;

public class MsAlreadyExistsException extends MicroServiceException {

    public MsAlreadyExistsException(String msg) {
        super(msg);
    }

    public MsAlreadyExistsException(Class<?> clazz, Tuple2<String, Object> data) {
        super(String.format(
                "Объект %s уже существует: %s : %s", clazz.getSimpleName(), data._1, data._2
        ));
    }

    public MsAlreadyExistsException(Class<?> clazz, Tuple4<String, Object, String, Object> data) {
        super(String.format(
                "Объект %s уже существует: %s : %s , %s : %s ", clazz.getSimpleName(), data._1, data._2, data._3, data._4
        ));
    }

    public MsAlreadyExistsException(Class<?> clazz, Tuple6<String, Object, String, Object, String, Object> data) {
        super(String.format(
                "Объект %s уже существует: %s : %s , %s : %s , %s : %s ", clazz.getSimpleName(), data._1, data._2, data._3, data._4, data._5, data._6
        ));
    }

}
