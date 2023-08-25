package com.cmc.domains.challenge.dto.response.my;

import com.cmc.challenge.Challenge;
import com.cmc.challenge.constant.JudgeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class JudgingChallengeResponseDto {

    @Schema(description = "챌린지 id", example = "10")
    private Long challengeId;

    @Schema(description = "챌린지 제목", example = "소비습관 고치기")
    private String challengeName;

    @Schema(description = "챌린지 분야", example = "재태크/돈관리/금융학습/소비절약")
    private String challengeBranch;

    @Schema(description = "챌린지 심사 상태", example = "JUDGING: 심사중, COMPLETE: 심사완료, FAILURE: 반려/실패")
    private String judgingStatus;

    @Schema(description = "챌린지 모집 시작일", example = "2023.07.27")
    private String recruitStartDate;

    public static JudgingChallengeResponseDto from(Challenge challenge){

        if(challenge.getJudgeStatus().equals(JudgeStatus.COMPLETE)){

            return new JudgingChallengeResponseDtoBuilder()
                    .challengeId(challenge.getChallengeId())
                    .challengeName(challenge.getChallengeName())
                    .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                    .judgingStatus(String.valueOf(challenge.getJudgeStatus()))
                    .recruitStartDate(challenge.getRecruitStartDate().getYear() + "." + challenge.getRecruitStartDate().getMonthValue() + "." +
                            challenge.getRecruitStartDate().getDayOfMonth())
                    .build();
        }
        else{
            return new JudgingChallengeResponseDtoBuilder()
                    .challengeId(challenge.getChallengeId())
                    .challengeName(challenge.getChallengeName())
                    .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                    .judgingStatus(String.valueOf(challenge.getJudgeStatus()))
                    .build();
        }
    }

}
