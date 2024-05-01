package com.stepanew.entities;

import java.util.List;

public record OneLineAnswer(

        String search,
        List<String> result,
        Long time

) {
}
