package com.cmc.domains.challenge.dto.response;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChallengeCreateResponseDto {

    @Schema(description = "챌린지 id", example = "10")
    private Long challengeId;

    public static ChallengeCreateResponseDto create(Challenge challenge) {
        return ChallengeCreateResponseDto.builder()
                .challengeId(challenge.getChallengeId())
                .build();
    }
}
