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
public class MemberUpdateResDto {

    @Schema(description = "멤버 id", example = "13")
    private Long memberId;

    @Schema(description = "멤버 이름", example = "건조한모래의사막")
    private String memberName;

    @Schema(description = "생년월일", example = "2023-07-09")
    private String memberBirth;

    @Schema(description = "성별", example = "F / M")
    private String memberGender;

    @Schema(description = "원하는 챌린지 분야", example = "T, M, L, C" )
    private String interestChallenge;

    public static MemberUpdateResDto from(Member member){

        return MemberUpdateResDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .memberBirth(member.getMemberBirth())
                .memberGender(member.getMemberGender())
                .interestChallenge(member.getInterestChallenge())
                .build();
    }
}
