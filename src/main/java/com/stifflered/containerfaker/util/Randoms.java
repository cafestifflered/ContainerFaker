package com.stifflered.containerfaker.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Randoms {

    public static <T> T randomIndex(T[] array) {
        if (array.length == 0) {
            return null;
        }

        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    public static <T> T randomIndex(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }

        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static int randomNumber(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

}
