package com.cmc.domains.challenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ChallengeParticipateRequestDto {

    @Schema(description = "참여할 챌린지 id", example = "10", required=true)
    private Long challengeId;

}
