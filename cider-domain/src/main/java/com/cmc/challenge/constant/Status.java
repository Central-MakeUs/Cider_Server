package com.cmc.challenge.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum Status {

    WAITING("W", "심사중"),
    RECRUITING("R", "모집중"),
    POSSIBLE("P", "참여 가능"),
    IMPOSSIBLE("I", "참여 불가능")
    ;

    private String alias;
    private String description;

    public static Optional<Status> of(String alias) {
        return Arrays.stream(values()).filter(S -> S.alias.equals(alias)).findFirst();
    }
}
