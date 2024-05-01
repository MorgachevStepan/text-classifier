package com.stepanew.utils;

import com.stepanew.entities.TerminalRequest;

public class TerminalService {

    private final TerminalRequest terminalRequest;

    private final static String DATA_FILE_COMMAND = "--data";
    private final static String INPUT_FILE_COMMAND = "--input-file";
    private final static String OUTPUT_FILE_COMMAND = "--output-file";
    private final static String OUTPUT_FILE_DEFAULT_PATH = "../src/main/resources/data/output-file.json";

    public TerminalService() {
        this.terminalRequest = new TerminalRequest();
    }

    public void handleTerminalRequest(String[] args) {
        int requestLength = args.length;

        for (int i = 0; i < requestLength; i++) {

            if (args[i].equals(DATA_FILE_COMMAND) && hasNext(i, requestLength)) {
                terminalRequest.setDataPath(args[++i]);
            }

            if (args[i].equals(INPUT_FILE_COMMAND) && hasNext(i, requestLength)) {
                terminalRequest.setInputPath(args[++i]);
            }

            if (args[i].equals(OUTPUT_FILE_COMMAND) && hasNext(i, requestLength)) {
                terminalRequest.setOutputPath(args[++i]);
            }

        }

        if(checkRequestIsValid()) {
            validateRequest();
        }
    }

    public String getDataPath() {
        return terminalRequest.getDataPath();
    }

    public String getInputPath() {
        return terminalRequest.getInputPath();
    }

    public String getOutputPath() {
        return terminalRequest.getOutputPath();
    }

    private boolean checkRequestIsValid() {
        if (terminalRequest.getDataPath() == null ||
                terminalRequest.getInputPath() == null) {
            throw new IllegalArgumentException("Incorrect terminal's arguments");
        }

        return true;
    }

    private void validateRequest() {
        if (terminalRequest.getOutputPath() == null) {
            terminalRequest.setOutputPath(OUTPUT_FILE_DEFAULT_PATH);
        }
    }

    private boolean hasNext(int i, int requestLength) {
        return i + 1 < requestLength;
    }

}
