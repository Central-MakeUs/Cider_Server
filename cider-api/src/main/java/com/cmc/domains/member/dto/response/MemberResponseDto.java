package com.cmc.domains.member.dto.response;

import com.cmc.member.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDto {

    private Long memberId;

    private String memberName;

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .build();
    }
}