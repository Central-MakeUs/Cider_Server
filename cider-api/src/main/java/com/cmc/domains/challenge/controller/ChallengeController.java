package com.cmc.domains.challenge.controller;

import com.cmc.challenge.Challenge;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.dto.response.ChallengeCreateResponseDto;
import com.cmc.domains.challenge.service.ChallengeService;
import com.cmc.global.resolver.RequestMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "challenge", description = "챌린지 API")
@RequestMapping("/api/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 생성 api")
    @PostMapping(value="")
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                    @RequestBody @Valid ChallengeCreateRequestDto req){

        Challenge challenge = challengeService.create(req, memberId);
        return new ResponseEntity<>(ChallengeCreateResponseDto.create(challenge), HttpStatus.CREATED);
    }

}
