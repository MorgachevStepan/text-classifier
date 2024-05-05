package com.stepanew.handlers;

import com.stepanew.analyzeres.FuzzyComparator;
import com.stepanew.entities.GeneralAnswer;
import com.stepanew.entities.OneLineAnswer;
import com.stepanew.entities.OneLineRequest;
import com.stepanew.entities.TemporaryAnswer;
import com.stepanew.normalizer.NormalizerService;
import com.stepanew.utils.CSVReader;
import com.stepanew.utils.OutputService;
import com.stepanew.utils.TerminalService;

import java.util.*;

public class RequestHandler {

    //подбирался экспериментальным путем
    private final static double SENTENCE_THRESHOLD = 0.7;
    private final static byte ANSWERS_SIZE = 3;
    private final String[] ARGS;
    private final FuzzyComparator fuzzyComparator;
    private final TerminalService terminalService;
    private final CSVReader csvReader;
    private final OutputService outputService;
    private final NormalizerService normalizerService;
    private final Map<String, List<String>> normalizedData;
    private final Map<String, List<String>> normalizedInput;
    private final Map<String, OneLineRequest> requestedData;
    private GeneralAnswer generalAnswer;


    public RequestHandler(String[] args) {
        ARGS = args;
        this.fuzzyComparator = new FuzzyComparator();
        this.terminalService = new TerminalService();
        this.csvReader = new CSVReader();
        this.normalizerService = new NormalizerService();
        this.outputService = new OutputService();
        this.normalizedData = new HashMap<>();
        this.normalizedInput = new HashMap<>();
        this.requestedData = new HashMap<>();
    }

    public void handleRequest() {
        long startTime = System.currentTimeMillis();
        terminalService.handleTerminalRequest(ARGS);
        prepareData();
        long endTime = System.currentTimeMillis();

        generalAnswer = new GeneralAnswer(endTime - startTime, new ArrayList<>());

        fuzzyCompare();
        outputService.writeOutput(generalAnswer, terminalService.getOutputPath());
    }

    private void fuzzyCompare() {
        PriorityQueue<TemporaryAnswer> answersQueue = new PriorityQueue<>(
                (a1, a2) -> Double.compare(a2.compareCoefficient(), a1.compareCoefficient())
        );

        for (Map.Entry<String, List<String>> input : normalizedInput.entrySet()) {
            long startTime = System.currentTimeMillis();
            for (Map.Entry<String, List<String>> data : normalizedData.entrySet()) {
                double compareCoefficient = fuzzyComparator.CalculateFuzzyEqual(input.getValue(), data.getValue());

                if (compareCoefficient >= SENTENCE_THRESHOLD) {
                    answersQueue.add(new TemporaryAnswer(requestedData.get(data.getKey()).UUID(), compareCoefficient));
                }
            }

            List<String> sortedAnswers = new ArrayList<>(ANSWERS_SIZE);

            while (!answersQueue.isEmpty() && sortedAnswers.size() < ANSWERS_SIZE) {
                sortedAnswers.add(answersQueue.poll().answer());
            }

            long endTime = System.currentTimeMillis();

            OneLineAnswer oneLineAnswer = new OneLineAnswer(input.getKey(), sortedAnswers, endTime - startTime);
            generalAnswer.getResult().add(oneLineAnswer);
        }
    }

    private void prepareData() {
        List<String> inputString = csvReader.readInputFile(terminalService.getInputPath());
        List<OneLineRequest> dataRequest = csvReader.readDataFile(terminalService.getDataPath());
        List<String> dataString = dataRequest.stream()
                .map(OneLineRequest::description)
                .toList();

        inputString
                .forEach(sentence -> normalizedInput.put(sentence, normalizerService.normalizeString(sentence)));

        dataString
                .forEach(sentence -> normalizedData.put(sentence, normalizerService.normalizeString(sentence)));

        dataRequest
                .forEach(request -> requestedData.put(request.description(), request));
    }

}
