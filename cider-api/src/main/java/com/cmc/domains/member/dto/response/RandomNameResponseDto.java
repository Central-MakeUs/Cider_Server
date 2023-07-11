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
public class RandomNameResponseDto {

    @Schema(description = "생성된 닉네임", example = "수익성있는이자사슴")
    private String randomName;

    public static RandomNameResponseDto from(String randomName){

        return RandomNameResponseDto.builder()
                .randomName(randomName)
                .build();
    }
}
