package com.cmc.domains.challengeLike.controller;


import com.cmc.common.response.CommonResponse;
import com.cmc.domains.challenge.service.ChallengeService;
import com.cmc.domains.challengeLike.dto.request.ChallengeLikeCreateRequestDto;
import com.cmc.domains.challengeLike.dto.response.ChallengeLikeCreateResponseDto;
import com.cmc.domains.challengeLike.service.ChallengeLikeService;
import com.cmc.global.resolver.RequestMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "challengeLike", description = "챌린지 관심 API")
@RequestMapping("/api/challenge/like")
public class ChallengeLikeController {

    private final ChallengeLikeService challengeLikeService;

    @Tag(name = "challengeLike")
    @Operation(summary = "관심 챌린지 등록 api")
    @PostMapping(value="")
    public ResponseEntity<CommonResponse> createChallengeLike(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                         @RequestBody @Valid ChallengeLikeCreateRequestDto req){

        challengeLikeService.create(memberId, req.getChallengeId());
        return ResponseEntity.ok(CommonResponse.from("관심 챌린지가 등록되었습니다"));
    }

    @Tag(name = "challengeLike")
    @Operation(summary = "관심 챌린지 삭제 api")
    @DeleteMapping(value="/{challengeId}")
    public ResponseEntity<CommonResponse> deleteChallengeLike(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                            @PathVariable("challengeId") Long challengeId) {

        challengeLikeService.delete(memberId, challengeId);
        return ResponseEntity.ok(CommonResponse.from("관심 챌린지가 삭제되었습니다"));
    }

}
