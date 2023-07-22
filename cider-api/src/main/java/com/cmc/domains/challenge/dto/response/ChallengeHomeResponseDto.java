package com.cmc.domains.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChallengeHomeResponseDto {

    @Schema(description = "인기 챌린지 리스트")
    private List<ChallengeResponseDto> challengeResponseDto;

    @Schema(description = "공식 챌린지 리스트")
    private List<ChallengeResponseDto> officialChallengeResponseDto;

    public static ChallengeHomeResponseDto from(List<ChallengeResponseDto> challengeResponseDto,
                                                List<ChallengeResponseDto> officialChallengeResponseDto){

        return new ChallengeHomeResponseDtoBuilder()
                .challengeResponseDto(challengeResponseDto)
                .officialChallengeResponseDto(officialChallengeResponseDto)
                .build();
    }

}
