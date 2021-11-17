package ru.marketboost.library.common.exceptions;

import io.vavr.*;

public class MsModelNotFoundException extends MicroServiceException {

    public MsModelNotFoundException(String message) {
        super(message);
    }

    public MsModelNotFoundException(Class<?> clazz, Tuple2<String, Object> data) {
        super(String.format(
                "Объект %s не найдет по %s : %s", clazz.getSimpleName(), data._1, data._2
        ));
    }

    public MsModelNotFoundException(Class<?> clazz, Tuple4<String, Object, String, Object> data) {
        super(String.format(
                "Объект %s не найдет по %s : %s , %s : %s ", clazz.getSimpleName(), data._1, data._2, data._3, data._4
        ));
    }

    public MsModelNotFoundException(Class<?> clazz, Tuple6<String, Object, String, Object, String, Object> data) {
        super(String.format(
                "Объект %s не найдет по %s : %s , %s : %s , %s : %s ", clazz.getSimpleName(), data._1, data._2, data._3, data._4, data._5, data._6
        ));
    }

}
