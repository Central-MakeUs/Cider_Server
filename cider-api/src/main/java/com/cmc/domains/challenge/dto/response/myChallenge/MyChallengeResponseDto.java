package com.cmc.domains.challenge.dto.response.myChallenge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MyChallengeResponseDto {

    @Schema(description = "진행중인 챌린지")
    private OngoingChallengeListResponseDto ongoingChallengeListResponseDto;

    @Schema(description = "최근 종료된 챌린지")
    private PassedChallengeListResponseDto passedChallengeListResponseDto;

    @Schema(description = "심사 중인 챌린지")
    private JudgingChallengeListResponseDto judgingChallengeListResponseDto;

    public static MyChallengeResponseDto from(OngoingChallengeListResponseDto ongoingChallengeListResponseDto, PassedChallengeListResponseDto passedChallengeListResponseDto,
                                       JudgingChallengeListResponseDto judgingChallengeListResponseDto){

        return new MyChallengeResponseDtoBuilder()
                .ongoingChallengeListResponseDto(ongoingChallengeListResponseDto)
                .passedChallengeListResponseDto(passedChallengeListResponseDto)
                .judgingChallengeListResponseDto(judgingChallengeListResponseDto)
                .build();
    }

}
