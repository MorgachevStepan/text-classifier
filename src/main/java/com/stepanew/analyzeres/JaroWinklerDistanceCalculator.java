package com.stepanew.analyzeres;

import java.util.Objects;

public class JaroWinklerDistanceCalculator {

    private static final double THRESHOLD_GAIN = 0.7;
    private static final int NUM_FIRST_CHARS = 2;
    private static final double TOTAL_MATCH = 1.0;
    private static final double TOTAL_MISMATCH = 0.0;
    private static final double WINKLER_COEFFICIENT = 0.1;

    public static double calculateJaroWinklerDistance(String firstWord, String secondWord) {
        if (Objects.equals(firstWord, secondWord)) {
            return TOTAL_MATCH;
        }

        int lengthFirst = firstWord.length();
        int lengthSecond = secondWord.length();

        //длина совпадений
        int searchRange = (int) (Math.floor(Math.max((double) lengthFirst, lengthSecond) / 2) - 1);

        boolean[] mathFirst = new boolean[lengthFirst];
        boolean[] matchSecond = new boolean[lengthSecond];

        int numComm = 0;
        //ищем неточные совпадения
        for (int i = 0; i < lengthFirst; ++i) {
            int startVal = Math.max(0, i - searchRange);
            int endVal = Math.min(i + searchRange + 1, lengthSecond);
            for (int j = startVal; j < endVal; ++j) {
                if (matchSecond[j]) continue;
                if (firstWord.charAt(i) != secondWord.charAt(j)) continue;
                mathFirst[i] = true;
                matchSecond[j] = true;
                ++numComm;
                break;
            }
        }

        if (numComm == 0){
            return TOTAL_MISMATCH;
        }

        //смотрим количество транспозиций для неточных совпадений
        int transpositions = 0;
        int point = 0;
        for (int i = 0; i < lengthFirst; ++i) {
            if (!mathFirst[i]) continue;

            while (!matchSecond[point]) {
                ++point;
            }

            if (firstWord.charAt(i) != secondWord.charAt(point)){
                ++transpositions;
            }

            ++point;
        }

        double halfTranspositions = transpositions / 2.0;

        //считаем расстояние Джаро
        double jaroDistance = ((double) numComm / lengthFirst + (double) numComm / lengthSecond + (numComm - halfTranspositions) / numComm) / 3.0;

        if (jaroDistance <= THRESHOLD_GAIN) {
            return jaroDistance;
        }

        int prefix = Math.min(NUM_FIRST_CHARS, Math.min(firstWord.length(), secondWord.length()));
        int position = 0;

        //если префиксы хоть сколько-то равны, то делается надбавка схожести
        while (position < prefix && firstWord.charAt(position) == secondWord.charAt(position)) {
            ++position;
        }

        if (position == 0) {
            return jaroDistance;
        }

        return jaroDistance + WINKLER_COEFFICIENT * position * (TOTAL_MATCH - jaroDistance);
    }

}
