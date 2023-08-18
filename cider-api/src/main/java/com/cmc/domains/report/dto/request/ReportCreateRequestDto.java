package com.cmc.domains.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Validated
public class ReportCreateRequestDto {

    @Schema(description = "신고/차단할 사용자 or 게시글 id", example = "20")
    private Long contentId;

    @Schema(description = "신고/차단 사유", example = "올바르지 않은 인증 내용 및 사진")
    private String reason;
}
