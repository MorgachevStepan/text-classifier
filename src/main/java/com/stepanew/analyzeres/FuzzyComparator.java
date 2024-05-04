package com.stepanew.analyzeres;

import java.util.ArrayList;
import java.util.List;

public class FuzzyComparator {

    private static final double WORD_THRESHOLD = 0.45;

    public double CalculateFuzzyEqual(List<String> firstNormalized, List<String> secondNormalized) {
        List<String> fuzzyEqualsTokens = getFuzzyEqualsTokens(firstNormalized, secondNormalized);

        int equalsCount = fuzzyEqualsTokens.size();
        int firstCount = firstNormalized.size();
        int secondCount = secondNormalized.size();

        return (1.0 * equalsCount) / (firstCount + secondCount - equalsCount);
    }

    private List<String> getFuzzyEqualsTokens(List<String> tokensFirst, List<String> tokensSecond) {
        List<String> equalsTokens = new ArrayList<>();
        boolean[] usedToken = new boolean[tokensSecond.size()];

        for (String s : tokensFirst) {
            for (int j = 0; j < tokensSecond.size(); j++) {
                if (!usedToken[j]) {
                    if (IsTokenFuzzyEqual(s, tokensSecond.get(j))) {
                        equalsTokens.add(s);
                        usedToken[j] = true;
                        break;
                    }
                }
            }
        }

        return equalsTokens;
    }

    private boolean IsTokenFuzzyEqual(String firstToken, String secondToken) {
        return JaroWinklerDistanceCalculator.calculateJaroWinklerDistance(firstToken, secondToken) >= WORD_THRESHOLD;
    }

}
