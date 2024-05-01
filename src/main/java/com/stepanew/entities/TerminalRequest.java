package com.stepanew.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TerminalRequest {

    String dataPath;
    String inputPath;
    String outputPath;

}
