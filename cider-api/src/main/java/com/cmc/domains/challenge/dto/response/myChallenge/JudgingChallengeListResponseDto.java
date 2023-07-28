package com.cmc.domains.challenge.dto.response.myChallenge;

import com.cmc.challenge.constant.JudgeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class JudgingChallengeListResponseDto {

    @Schema(description = "심사 중인 챌린지 개수", example = "3")
    private Integer judgingChallengeNum;

    @Schema(description = "심사 완료 개수", example = "1")
    private Integer completeNum;

    @Schema(description = "심사 중인 챌린지 리스트")
    private List<JudgingChallengeResponseDto> judgingChallengeResponseDtoList;

    public static JudgingChallengeListResponseDto from(List<JudgingChallengeResponseDto> judgingChallengeResponseDtoList){

        int cnt = 0;
        for(JudgingChallengeResponseDto dto : judgingChallengeResponseDtoList){
            if(dto.getJudgingStatus().equals("COMPLETE")){
                cnt += 1;
            }
        }

        return new JudgingChallengeListResponseDtoBuilder()
                .judgingChallengeNum(judgingChallengeResponseDtoList.size())
                .completeNum(cnt)
                .judgingChallengeResponseDtoList(judgingChallengeResponseDtoList)
                .build();
    }

}