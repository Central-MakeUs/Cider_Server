package com.cmc.domains.challenge.dto.response.my;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Data
@Builder
@AllArgsConstructor
@Slf4j
public class OngoingChallengeResponseDto {

    @Schema(description = "챌린지 제목", example = "소비습관 고치기")
    private String challengeName;

    @Schema(description = "챌린지 분야", example = "TECHNOLOGY: 재태크, SAVING: 소비절약, LEARNING: 금융학습, MONEY: 돈관리")
    private String challengeBranch;

    @Schema(description = "챌린지 진행일", example = "챌린지 진행 +10일의 10")
    private Long ongoingDate;

    @Schema(description = "챌린지 기간", example = "'30회 중 24회 달성'의 30")
    private Integer challengePeriod;

    @Schema(description = "인증 횟수", example = "'30회 중 24회 달성'의 24")
    private Integer certifyNum;

    public static OngoingChallengeResponseDto from(Challenge challenge, Integer certifyNum){

        log.info("challengeID :::::::: " + challenge.getChallengeId());
        return new OngoingChallengeResponseDtoBuilder()
                .challengeName(challenge.getChallengeName())
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .ongoingDate(ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), LocalDateTime.now()))
                .challengePeriod(challenge.getChallengePeriod())
                .certifyNum(certifyNum)
                .build();
    }

}
