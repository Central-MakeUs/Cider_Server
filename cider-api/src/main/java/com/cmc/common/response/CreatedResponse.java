package com.cmc.common.response;

import com.cmc.challenge.Challenge;
import com.cmc.domains.challenge.dto.response.ChallengeCreateResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreatedResponse {

    @Schema(description = "id", example = "10")
    private Long createdId;

    public static CreatedResponse create(Long createdId) {
        return CreatedResponse.builder()
                .createdId(createdId)
                .build();
    }
}
