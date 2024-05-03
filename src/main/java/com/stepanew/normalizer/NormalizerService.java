package com.stepanew.normalizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizerService {

    private final static String STOP_WORDS_PATH = "src/main/resources/data/stop-words.txt";
    private final List<String> stopWords;
    private final Stemmer stemmer;

    public NormalizerService() {
        this.stopWords = getStopWords();
        this.stemmer = new Stemmer();
    }

    public List<String> normalizeString(String inputString) {
        String inputStringToLowerCase = inputString.toLowerCase();
        String stringWithoutNoise = removeNoise(inputStringToLowerCase);
        List<String> words = textToWords(stringWithoutNoise);
        List<String> withoutStopWords = removeStopWords(words);

        return stem(withoutStopWords);
    }

    private List<String> stem(List<String> withoutStopWords) {
        return withoutStopWords.stream()
                .map(stemmer::stem)
                .collect(Collectors.toList());
    }

    private List<String> textToWords(String stringWithoutNoise) {
        String[] wordsArray = stringWithoutNoise.split("\\s");

        return new ArrayList<>(Arrays.asList(wordsArray));
    }

    private List<String> getStopWords() {
        List<String> result = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(STOP_WORDS_PATH, StandardCharsets.UTF_8))) {
            String oneLine;

            while ((oneLine = bufferedReader.readLine()) != null) {
                result.add(oneLine);
            }

        } catch (IOException e) {
            throw new RuntimeException("Can't open data file");
        }

        return result;
    }

    private List<String> removeStopWords(List<String> stringWithoutNoise) {
        List<String> result = new ArrayList<>();
        for (String word : stringWithoutNoise) {
            if (!stopWords.contains(word)) {
                result.add(word);
            }
        }

        return result;
    }

    private String removeNoise(String inputStringToLowerCase) {
        return inputStringToLowerCase.chars()
                .mapToObj(c -> (char) c)
                .filter(this::isNormalizedCharacter)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private boolean isNormalizedCharacter(char symbol) {
        return Character.isLetterOrDigit(symbol) || symbol == ' ';
    }

}
