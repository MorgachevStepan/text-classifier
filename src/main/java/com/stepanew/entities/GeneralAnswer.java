package com.stepanew.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeneralAnswer {

    Long initTime;
    List<OneLineAnswer> result;

}
