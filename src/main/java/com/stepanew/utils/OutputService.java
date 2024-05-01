package com.stepanew.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepanew.entities.GeneralAnswer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class OutputService {

    public void writeOutput(GeneralAnswer outputData, String outputPath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonOutputData = objectMapper.writeValueAsString(outputData);

            Path filePath = Path.of(outputPath);

            Files.writeString(
                    filePath,
                    jsonOutputData,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize response");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to output file");
        }
    }

}
