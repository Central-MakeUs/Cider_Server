package com.cmc.member.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@Getter
@AllArgsConstructor
public enum MemberLevel {

    LEVEL_1(1, "LV 1 시작 챌린저"),
    LEVEL_2(2, "LV 2 성실한 챌린저"),
    LEVEL_3(3, "LV 3 능숙한 챌린저"),
    LEVEL_4(4, "LV 4 열정적인 챌린저"),
    LEVEL_5(5, "LV 5 엘리트 챌린저"),
    LEVEL_6(6, "LV 6 챌린지 마스터")
    ;

    private Integer level;

    private String levelName;

    // TODO : 확인
    public static String getLevelNameByLevel(Long level) {

        return Arrays.stream(values()).filter(lv -> lv.level.equals(level)).map(MemberLevel::getLevelName).toString();
    }
}
