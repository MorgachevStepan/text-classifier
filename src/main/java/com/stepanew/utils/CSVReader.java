package com.stepanew.utils;

import com.stepanew.entities.OneLineRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public List<OneLineRequest> readDataFile(String dataPath) {
        List<OneLineRequest> data = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dataPath))) {
            String oneLine;

            while ((oneLine = bufferedReader.readLine()) != null) {
                String[] fields = oneLine.split("\\|");

                if (fields.length != 3) {
                    throw new IOException("Incorrect csv`s data");
                }

                addOneLineRequest(data, fields);
            }

        } catch (IOException e) {
            throw new RuntimeException("Can't open data file");
        }

        return data;
    }

    public List<String> readInputFile(String inputPath) {
        List<String> data = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputPath))) {
            String oneLine;

            while ((oneLine = bufferedReader.readLine()) != null) {
                data.add(oneLine);
            }

            if (data.isEmpty()) {
                throw new IOException("Input file must not be empty");
            }

        } catch (IOException e) {
            throw new RuntimeException("Can't open data file");
        }

        return data;
    }

    private void addOneLineRequest(List<OneLineRequest> data, String[] fields) {
        data.add(
                new OneLineRequest(
                        fields[0].trim(),
                        fields[1].trim(),
                        fields[2].trim()
                )
        );
    }

}
