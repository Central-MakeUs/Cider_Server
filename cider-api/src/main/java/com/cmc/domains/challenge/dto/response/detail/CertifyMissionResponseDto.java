package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CertifyMissionResponseDto {

    private String certifyMission;

    private String successExampleImage;

    private String failureExampleImage;

    public static CertifyMissionResponseDto from(Challenge challenge, String successExampleImage, String failureExampleImage){

        return new CertifyMissionResponseDtoBuilder()
                .certifyMission(challenge.getCertifyMission())
                .successExampleImage(successExampleImage)
                .failureExampleImage(failureExampleImage)
                .build();
    }
}
