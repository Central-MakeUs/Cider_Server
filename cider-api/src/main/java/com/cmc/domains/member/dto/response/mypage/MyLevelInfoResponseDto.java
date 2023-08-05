package com.cmc.domains.member.dto.response.mypage;

import com.cmc.domains.member.dto.response.LevelInfoResponseDto;
import com.cmc.member.Member;
import com.cmc.memberLevel.MemberLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyLevelInfoResponseDto {

    private Integer myLevel;

    private Integer levelPercent;

    private String percentComment;

    private Integer experienceLeft;

    private String myLevelName;

    private LevelInfoResponseDto currentLevel;

    private LevelInfoResponseDto nextLevel;

    public static MyLevelInfoResponseDto from(Member member, LevelInfoResponseDto currentLevel, LevelInfoResponseDto nextLevel){

        return new MyLevelInfoResponseDtoBuilder()
                .myLevel(member.getMemberLevel().getMemberLevelId())
                .levelPercent((int) ((member.getMemberExperience() / (nextLevel.getRequiredExperience() - currentLevel.getRequiredExperience())) * 0.01))
                .percentComment(MemberLevel.getPercentComment((int) ((member.getMemberExperience() / (nextLevel.getRequiredExperience() - currentLevel.getRequiredExperience())) * 0.01)))
                .experienceLeft(nextLevel.getRequiredExperience() - member.getMemberExperience())
                .myLevelName(member.getMemberLevel().getLevelName())
                .currentLevel(currentLevel)
                .nextLevel(nextLevel)
                .build();
    }
}
