package ru.marketboost.ransom.utils;

import java.util.Random;

public class Randomizer {

    public static int randomBetween(int min, int max) {
        return new Random().nextInt(max + 1 - min) + min;
    }

}
