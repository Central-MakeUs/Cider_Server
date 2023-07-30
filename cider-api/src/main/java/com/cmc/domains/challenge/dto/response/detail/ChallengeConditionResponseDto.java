package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@AllArgsConstructor
public class ChallengeConditionResponseDto {

    @Schema(description = "챌린지 기간", example = "'30회 중 6회 진행의 30")
    private Integer challengePeriod;

    @Schema(description = "챌린지 진행일", example = "30회 중 6회 진행의 6")
    private Long ongoingDate;

    @Schema(description = "평균 현황", example = "% 기준")
    private Integer averageCondition;

    @Schema(description = "나의 현황", example = "% 기준")
    private Integer myCondition;

    public static ChallengeConditionResponseDto from(Challenge challenge, Integer myCondition){

        return new ChallengeConditionResponseDtoBuilder()
                .challengePeriod(challenge.getChallengePeriod())
                .ongoingDate(ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), LocalDateTime.now()))
                .averageCondition(challenge.getAverageCondition())
                .myCondition(myCondition)
                .build();
    }
}
