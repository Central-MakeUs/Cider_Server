package com.cmc.domains.challenge.dto.response.myChallenge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PassedChallengeListResponseDto {

    @Schema(description = "최근 종료된 챌린지 개수", example = "3")
    private Integer passedChallengeNum;

    @Schema(description = "최근 종료된 챌린지 리스트")
    private List<PassedChallengeResponseDto> passedChallengeResponseDtoList;

    public static PassedChallengeListResponseDto from(List<PassedChallengeResponseDto> passedChallengeResponseDtoList){

        return new PassedChallengeListResponseDtoBuilder()
                .passedChallengeNum(passedChallengeResponseDtoList.size())
                .passedChallengeResponseDtoList(passedChallengeResponseDtoList)
                .build();
    }
}
