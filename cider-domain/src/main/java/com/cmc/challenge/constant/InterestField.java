package com.cmc.challenge.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum InterestField {

    TECHNOLOGY("T", "재태크"),
    MONEY("M", "돈관리"),
    LEARNING("L", "금융학습"),
    SAVING("C", "소비절약")
    ;

    private String alias;
    private String description;

    public static InterestField of(String alias) {
        return Arrays.stream(values())
                .filter(f -> f.alias.equals(alias))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("InterestField's alias is not found"));
    }
}
