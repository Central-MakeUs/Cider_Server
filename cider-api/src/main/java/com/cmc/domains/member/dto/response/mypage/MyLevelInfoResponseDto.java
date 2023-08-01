package com.cmc.domains.member.dto.response.mypage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyLevelInfoResponseDto {

    private String comment;

    private Integer memberLevel;

    private String levelName;

    // TODO : 기획에 질문 중
}
