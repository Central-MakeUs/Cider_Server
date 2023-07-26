package com.cmc.domains.certify.dto.response;

import com.cmc.certify.Certify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CertifyCreateResponseDto {

    @Schema(description = "챌린지 인증 id", example = "10")
    private Long certifyId;

    public static CertifyCreateResponseDto create(Certify certify) {
        return CertifyCreateResponseDto.builder()
                .certifyId(certify.getCertifyId())
                .build();
    }
}

