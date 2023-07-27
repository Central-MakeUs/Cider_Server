package com.cmc.domains.member.dto.response;

import com.cmc.member.Member;
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

    @Schema(description = "멤버 레벨", example = "5")
    private Integer memberLevel;

    public static SimpleMemberResponseDto from(Member member) {

        return SimpleMemberResponseDto.builder()
                .memberName(member.getMemberName())
                .profilePath(member.getProfilePath())
                .memberLevel(member.getMemberLevel())
                .build();
    }
}
