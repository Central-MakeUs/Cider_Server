package com.cmc.domains.challenge.dto.response;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MyParticipateChallengeResponseDto {

    @Schema(description = "챌린지 id", example = "12")
    private Long challengeId;

    @Schema(description = "챌린지 제목", example = "소비습관 고치기")
    private String challengeName;


    @Schema(description = "챌린지 참여 상태", example = "ONGOING: 진행중, SUCCESS: 성공, FAILURE: 실패")
    private String participateStatus;

    public static MyParticipateChallengeResponseDto from(Challenge challenge, String participateStatus){

        return new MyParticipateChallengeResponseDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .challengeName(challenge.getChallengeName())
                .participateStatus(participateStatus)
                .build();
    }

}
