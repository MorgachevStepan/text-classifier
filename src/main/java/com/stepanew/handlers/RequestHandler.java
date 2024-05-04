package com.stepanew.handlers;

import com.stepanew.analyzeres.FuzzyComparator;
import com.stepanew.entities.OneLineRequest;
import com.stepanew.normalizer.NormalizerService;
import com.stepanew.utils.CSVReader;
import com.stepanew.utils.TerminalService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler {

    private final static double SENTENCE_THRESHOLD = 0.3;
    private final String[] ARGS;
    private final FuzzyComparator fuzzyComparator;
    private final TerminalService terminalService;
    private final CSVReader csvReader;
    private final NormalizerService normalizerService;
    private final Map<String, List<String>> normalizedData;
    private final Map<String, List<String>> normalizedInput;


    public RequestHandler(String[] args) {
        ARGS = args;
        this.fuzzyComparator = new FuzzyComparator();
        this.terminalService = new TerminalService();
        this.csvReader = new CSVReader();
        this.normalizerService = new NormalizerService();
        this.normalizedData = new HashMap<>();
        this.normalizedInput = new HashMap<>();
    }

    public void handleRequest() {
        terminalService.handleTerminalRequest(ARGS);
        prepareData();

        for (Map.Entry<String, List<String>> input: normalizedInput.entrySet()) {
            for (Map.Entry<String, List<String>> data: normalizedData.entrySet()) {
                if(fuzzyComparator.CalculateFuzzyEqual(input.getValue(), data.getValue()) >= SENTENCE_THRESHOLD) {
                    System.out.println("Проверку прошли: ");
                    System.out.println(input.getKey());
                    System.out.println(data.getKey());
                }
            }
        }
    }

    private void prepareData() {
        List<String> inputString = csvReader.readInputFile(terminalService.getInputPath());
        List<String> dataString = csvReader.readDataFile(terminalService.getDataPath())
                .stream()
                .map(OneLineRequest::description)
                .toList();

        inputString
                .forEach(sentence -> normalizedInput.put(sentence, normalizerService.normalizeString(sentence)));

        dataString
                .forEach(sentence -> normalizedData.put(sentence, normalizerService.normalizeString(sentence)));
    }

}
