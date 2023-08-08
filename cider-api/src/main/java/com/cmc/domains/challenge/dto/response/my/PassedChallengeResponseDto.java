package com.cmc.domains.challenge.dto.response.my;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PassedChallengeResponseDto {

    @Schema(description = "챌린지 id", example = "10")
    private Long challengeId;

    @Schema(description = "챌린지 제목", example = "소비습관 고치기")
    private String challengeName;

    @Schema(description = "챌린지 분야", example = "재태크/돈관리/금융학습/소비절약")
    private String challengeBranch;

    @Schema(description = "챌린지 진행 기간", example = "4")
    private Integer challengePeriod;

    @Schema(description = "공식 챌린지 여부", example = "true")
    private Boolean isOfficial;

    @Schema(description = "챌린지 리워드 여부", example = "true")
    private Boolean isReward;

    @Schema(description = "나의 챌린지 성공 여부", example = "SUCCESS/FAILURE")
    private String isSuccess;

    @Schema(description = "챌린지 성공 인원", example = "20")
    private Integer successNum;

    public static PassedChallengeResponseDto from(Challenge challenge, String isSuccess, Integer successNum){

        return new PassedChallengeResponseDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .challengeName(challenge.getChallengeName())
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .challengePeriod(challenge.getChallengePeriod())
                .isReward(challenge.getIsReward())
                .isOfficial(challenge.getIsOfficial())
                .isSuccess(isSuccess)
                .successNum(successNum)
                .build();

    }

}
