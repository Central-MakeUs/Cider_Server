package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CertifyMissionResponseDto {

    @Schema(description = "인증 미션", example = "하루 한 번 아침에 일어나서 물사진 인증")
    private String certifyMission;

    @Schema(description = "성공 예시 사진", example = "image url")
    private String successExampleImage;

    @Schema(description = "실패 예시 사진", example = "image url")
    private String failureExampleImage;

    public static CertifyMissionResponseDto from(Challenge challenge, String successExampleImage, String failureExampleImage){

        return new CertifyMissionResponseDtoBuilder()
                .certifyMission(challenge.getCertifyMission())
                .successExampleImage(successExampleImage)
                .failureExampleImage(failureExampleImage)
                .build();
    }
}
