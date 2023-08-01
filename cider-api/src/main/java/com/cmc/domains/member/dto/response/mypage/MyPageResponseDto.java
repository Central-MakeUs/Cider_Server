package com.cmc.domains.member.dto.response.mypage;

import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPageResponseDto {

    private SimpleMemberResponseDto simpleMember;

    private MyActivityInfoResponseDto memberActivityInfo;

    private MyLevelInfoResponseDto memberLevelInfo;

    public static MyPageResponseDto from(SimpleMemberResponseDto simpleMemberResponseDto, MyActivityInfoResponseDto memberActivityInfo, MyLevelInfoResponseDto memberLevelInfo){

        return new MyPageResponseDtoBuilder()
                .simpleMember(simpleMemberResponseDto)
                .memberActivityInfo(memberActivityInfo)
                .memberLevelInfo(memberLevelInfo)
                .build();
    }
}
