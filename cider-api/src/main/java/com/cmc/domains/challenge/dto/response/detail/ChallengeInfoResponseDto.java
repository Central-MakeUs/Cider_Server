package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChallengeInfoResponseDto {

    private String recruitPeriod;

    private String challengePeriod;

    private Integer challengeCapacity;

    private Integer certifyNum;

    private String certifyTime;

    private Boolean isReward;

    public static ChallengeInfoResponseDto from(Challenge challenge, String recruitPeriod, String challengePeriod){

        return new ChallengeInfoResponseDtoBuilder()
                .recruitPeriod(recruitPeriod)
                .challengePeriod(challengePeriod)
                .challengeCapacity(challenge.getChallengeCapacity())
                .certifyNum(challenge.getCertifyNum())
                .certifyTime("매일 자정까지")
                .isReward(challenge.getIsReward())
                .build();
    }
}
