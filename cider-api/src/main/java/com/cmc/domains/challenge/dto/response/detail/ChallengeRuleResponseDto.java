package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChallengeRuleResponseDto {

    private String failureRule;

    private String certifyRule;

    public static ChallengeRuleResponseDto from(Challenge challenge){

        return new ChallengeRuleResponseDtoBuilder()
                .failureRule(challenge.getFailureRule())
                .certifyRule("실시간 사진촬영")
                .build();
    }

}
