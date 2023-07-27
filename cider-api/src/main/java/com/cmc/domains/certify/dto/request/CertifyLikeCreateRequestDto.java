package com.cmc.domains.certify.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CertifyLikeCreateRequestDto {

    @Schema(description = "인증 id", example = "10", required=true)
    private Long certifyId;

}
