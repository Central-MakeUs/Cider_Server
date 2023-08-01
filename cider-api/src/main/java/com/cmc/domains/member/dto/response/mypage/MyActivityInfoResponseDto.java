package com.cmc.domains.member.dto.response.mypage;

import com.cmc.member.Member;
import com.cmc.participate.Participate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyActivityInfoResponseDto {

    private Integer myLevel;

    private Long myCertifyNum;

    private Long myLikeChallengeNum;

    public static MyActivityInfoResponseDto from (Member member){

        Long myCertifyNum = 0L;
        for(Participate participate : member.getParticipates()){
            myCertifyNum += participate.getCertifies().size();
        }

        return new MyActivityInfoResponseDtoBuilder()
                .myLevel(member.getMemberLevel())
                .myCertifyNum(myCertifyNum)
                .myLikeChallengeNum((long) member.getChallengeLikes().size())
                .build();
    }
}
