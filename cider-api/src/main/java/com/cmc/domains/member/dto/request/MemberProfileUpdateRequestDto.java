package com.cmc.domains.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class MemberProfileUpdateRequestDto {

    @NotNull
    @Schema(description = "멤버 이름", example = "건조한모래의사막", required=true)
    private String memberName;

    @NotNull
    @Schema(description = "멤버 소개", example = "안녕하세요", required=true)
    private String memberIntro;
}
