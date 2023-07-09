package com.cmc.domains.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class MemberUpdateReqDto {

    @NotNull
    @Schema(description = "멤버 이름", example = "건조한모래의사막", required=true)
    private String memberName;

    @NotNull
    @Schema(description = "생년월일", example = "2023-07-09", required=true)
    private String memberBirth;

    @NotNull
    @Schema(description = "성별 - F: 여성, M: 남성", example = "F / M", required=true)
    private String memberGender;

    @NotNull
    @Schema(description = "원하는 챌린지 분야 - T: 재테크, M: 돈관리, L: 금융학습, C: 소비절약\n" + ",로 구분해서 보내주세요", example = "T, M, L, C", required=true)
    private String interestChallenge;

}