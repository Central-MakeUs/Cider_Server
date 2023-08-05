package com.cmc.domains.member.dto.response;

import com.cmc.memberLevel.MemberLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LevelInfoResponseDto {

    private Integer level;

    private String levelName;

    private Integer requiredExperience;

    public static LevelInfoResponseDto from(MemberLevel memberLevel){

        return new LevelInfoResponseDtoBuilder()
                .level(memberLevel.getMemberLevelId())
                .levelName(memberLevel.getLevelName())
                .requiredExperience(memberLevel.getRequiredExperience())
                .build();
    }

}
