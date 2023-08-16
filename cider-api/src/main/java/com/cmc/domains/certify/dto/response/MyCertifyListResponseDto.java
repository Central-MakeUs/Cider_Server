package com.cmc.domains.certify.dto.response;

import com.cmc.domains.challenge.dto.response.SimpleChallengeResponseDto;
import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MyCertifyListResponseDto {

    @Schema(description = "간단 멤버 정보")
    private SimpleMemberResponseDto simpleMemberResponseDto;

    @Schema(description = "간단 챌린지 정보")
    private SimpleChallengeResponseDto simpleChallengeResponseDto;

    @Schema(description = "인증글 리스트")
    private List<SimpleCertifyResponseDtoV2> certifyResponseDtoList;

    public static MyCertifyListResponseDto from(SimpleMemberResponseDto simpleMemberResponseDto, SimpleChallengeResponseDto simpleChallengeResponseDto,
                                                List<SimpleCertifyResponseDtoV2> simpleCertifyResponseDtos){

        return new MyCertifyListResponseDtoBuilder()
                .simpleMemberResponseDto(simpleMemberResponseDto)
                .simpleChallengeResponseDto(simpleChallengeResponseDto)
                .certifyResponseDtoList(simpleCertifyResponseDtos)
                .build();
    }


}
