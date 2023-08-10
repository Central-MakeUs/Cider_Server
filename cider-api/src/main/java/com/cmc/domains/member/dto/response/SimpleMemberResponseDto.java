package com.cmc.domains.member.dto.response;

import com.cmc.member.Member;
import com.cmc.member.constant.MemberLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMemberResponseDto {

    @Schema(description = "멤버 이름", example = "건조한모래사막")
    private String memberName;

    @Schema(description = "프로필 이미지 url", example = "profileurl~")
    private String profilePath;

    @Schema(description = "멤버 레벨", example = "LV 2 성실한 챌린저")
    private String memberLevelName;

    @Schema(description = "n번째 챌린지", example = "3")
    private Integer participateChallengeNum;

    public static SimpleMemberResponseDto from(Member member) {

        return SimpleMemberResponseDto.builder()
                .memberName(member.getMemberName())
                .profilePath(member.getProfilePath())
                .memberLevelName("LV " + member.getMemberLevel().getMemberLevelId() + " " + member.getMemberLevel().getLevelName())
                .participateChallengeNum(member.getParticipates().size())    // TODO: 반려된 챌린지 예외처리
                .build();
    }
}
