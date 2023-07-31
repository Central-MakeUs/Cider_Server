package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChallengeRuleResponseDto {

    @Schema(description = "실패 기준", example = "11회 미만 인증")
    private String failureRule;

    @Schema(description = "인증 방법", example = "실시간 사진촬영")
    private String certifyRule;

    public static ChallengeRuleResponseDto from(Challenge challenge){

        return new ChallengeRuleResponseDtoBuilder()
                .failureRule(challenge.getFailureRule())
                .certifyRule("실시간 사진촬영")
                .build();
    }

}
