package com.cmc.domains.challengeLike.dto.request;

import com.cmc.challenge.Challenge;
import com.cmc.challengeLike.ChallengeLike;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ChallengeLikeCreateRequestDto {

    @Schema(description = "챌린지 id", example = "10", required=true)
    private Long challengeId;

}
