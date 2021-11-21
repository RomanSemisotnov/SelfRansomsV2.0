package ru.marketboost.library.common.utils;

import java.util.Random;

public class Randomizer {

    private static final Random r = new Random();

    public static int randomBetween(int min, int max) {
        return r.nextInt(max + 1 - min) + min;
    }

    public static double randomBetween(double min, double max) {
        return min + (max - min) * r.nextDouble();
    }

}
