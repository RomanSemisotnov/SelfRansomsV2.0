package ru.marketboost.library.common.utils;

import java.util.*;

public class Collections {


    public static <T> List<T> getRandomElements(List<? extends T> givenList, int numberOfElements) {
        Random rand = new Random();
        List<T> newColl = new ArrayList<>();

        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(givenList.size());
            T randomElement = givenList.get(randomIndex);
            givenList.remove(randomIndex);
            newColl.add(randomElement);
        }
        return newColl;
    }


}
