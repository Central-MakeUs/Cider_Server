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
    private List<ChallengeResponseDto> popularChallengeResponseDto;

    @Schema(description = "공식 챌린지 리스트")
    private List<ChallengeResponseDto> officialChallengeResponseDto;

    public static ChallengeHomeResponseDto from(List<ChallengeResponseDto> popularChallengeResponseDto,
                                                List<ChallengeResponseDto> officialChallengeResponseDto){

        return new ChallengeHomeResponseDtoBuilder()
                .popularChallengeResponseDto(popularChallengeResponseDto)
                .officialChallengeResponseDto(officialChallengeResponseDto)
                .build();
    }

}
