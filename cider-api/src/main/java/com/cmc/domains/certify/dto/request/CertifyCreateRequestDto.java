package com.cmc.domains.certify.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CertifyCreateRequestDto {

    @Schema(description = "챌린지 id", example = "10", required=true)
    private Long challengeId;

    @Schema(description = "인증 제목", example = "오늘도 인증합니다~", required=true)
    private String certifyName;

    @Schema(description = "인증 내용", example = "커피값을 아꼈어요", required=true)
    private String certifyContent;
}
