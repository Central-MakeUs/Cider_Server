package com.cmc.domains.challenge.dto.response.my;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OngoingChallengeListResponseDto {

    @Schema(description = "진행 중인 중인 챌린지 개수", example = "3")
    private Integer ongoingChallengeNum;

    @Schema(description = "진행 중인 중인 챌린지 리스트")
    private List<OngoingChallengeResponseDto> ongoingChallengeResponseDtoList;

    public static OngoingChallengeListResponseDto from(List<OngoingChallengeResponseDto> ongoingChallengeResponseDtoList){

        return new OngoingChallengeListResponseDtoBuilder()
                .ongoingChallengeNum(ongoingChallengeResponseDtoList.size())
                .ongoingChallengeResponseDtoList(ongoingChallengeResponseDtoList)
                .build();
    }
}
