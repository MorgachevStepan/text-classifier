package com.stepanew.entities;

import java.util.List;

public record GeneralAnswer(

        Long initTime,
        List<OneLineAnswer> result

) {
}
