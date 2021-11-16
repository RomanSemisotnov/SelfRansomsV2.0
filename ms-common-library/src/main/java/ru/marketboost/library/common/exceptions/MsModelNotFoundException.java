package ru.marketboost.library.common.exceptions;

import io.vavr.*;

public class MsModelNotFoundException extends MicroServiceException {

    public MsModelNotFoundException(Class<?> clazz, Tuple2<String, Object> data) {
        super(String.format(
                "Object %s wasnt founded by %s : %s", clazz.getSimpleName(), data._1, data._2
        ));
    }

    public MsModelNotFoundException(Class<?> clazz, Tuple4<String, Object, String, Object> data) {
        super(String.format(
                "Object %s wasnt founded by %s : %s , %s : %s ", clazz.getSimpleName(), data._1, data._2, data._3, data._4
        ));
    }

    public MsModelNotFoundException(Class<?> clazz, Tuple6<String, Object, String, Object, String, Object> data) {
        super(String.format(
                "Object %s wasnt founded by %s : %s , %s : %s , %s : %s ", clazz.getSimpleName(), data._1, data._2, data._3, data._4, data._5, data._6
        ));
    }

}
