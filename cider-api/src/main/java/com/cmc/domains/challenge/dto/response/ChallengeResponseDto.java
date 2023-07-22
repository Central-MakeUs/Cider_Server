package com.cmc.domains.challenge.dto.response;

import com.cmc.challenge.Challenge;
import com.cmc.challenge.constant.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChallengeResponseDto {

    @Schema(description = "챌린지 id", example = "10")
    private Long challengeId;

    @Schema(description = "챌린지 제목", example = "소비습관 고치기")
    private String challengeName;

    @Schema(description = "챌린지 상태", example = "RECRUITING: 모집중, POSSIBLE: 참여 가능, IMPOSSIBLE: 참여 불가(종료)")
    private Status challengeStatus;

    @Schema(description = "챌린지 대기/참여중 인원", example = "5")
    private Integer participateNum;

    @Schema(description = "모집중인 경우 - 디데이", example = "23")
    private Long recruitLeft;

    @Schema(description = "챌린지 분야", example = "재태크/돈관리/금융학습/소비절약")
    private String interestField;

    @Schema(description = "챌린지 진행 기간", example = "4(주)")
    private Integer challengePeriod;

    @Schema(description = "공식 챌린지 여부", example = "true")
    private Boolean isOfficial;

    @Schema(description = "리워드 여부", example = "true")
    private Boolean isReward;

    @Schema(description = "로그인 한 사용자 - 챌린지 좋아요 여부", example = "false")
    private Boolean isLike;

    public static ChallengeResponseDto from(Challenge challenge, Integer participateNum, Boolean isLike, Long recruitLeft){

        return new ChallengeResponseDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .challengeName(challenge.getChallengeName())
                .challengeStatus(challenge.getChallengeStatus())
                .participateNum(participateNum)
                .recruitLeft(recruitLeft)
                .interestField(challenge.getChallengeBranch())
                .isOfficial(challenge.getIsOfficial())
                .isReward(challenge.getIsReward())
                .isLike(isLike)
                .build();
    }

}
