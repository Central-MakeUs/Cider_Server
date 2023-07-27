package com.cmc.domains.challenge.dto.response;

import com.cmc.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SimpleChallengeResponseDto {

    private String challengeName;

    private String challengeBranch;

    private Integer participateNum;

    public static SimpleChallengeResponseDto from(Challenge challenge) {

        return SimpleChallengeResponseDto.builder()
                .challengeName(challenge.getChallengeName())
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .participateNum(challenge.getParticipates().size())
                .build();
    }
}
