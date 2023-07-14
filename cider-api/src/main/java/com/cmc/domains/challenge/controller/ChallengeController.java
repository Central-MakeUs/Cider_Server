package com.cmc.domains.challenge.controller;

import com.cmc.challenge.Challenge;
import com.cmc.common.response.CommonResponse;
import com.cmc.common.response.CreatedResponse;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.dto.response.ChallengeCreateResponseDto;
import com.cmc.domains.challenge.service.ChallengeService;
import com.cmc.domains.image.service.ImageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "challenge", description = "챌린지 API")
@RequestMapping("/api/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ImageService imageService;

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 생성 api")
    @PostMapping(value="")
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                    @RequestBody @Valid ChallengeCreateRequestDto req){

        Challenge challenge = challengeService.create(req, memberId);
        return new ResponseEntity<>(ChallengeCreateResponseDto.create(challenge), HttpStatus.CREATED);
    }

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 인증 성공 예시 이미지 업로드 api")
    @PostMapping(value="/images/success/{challengeId}")
    public ResponseEntity<CommonResponse> createSuccessExampleImages(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                            @RequestPart(value = "certifyExampleImages") List<MultipartFile> certifyExampleImages,
                                                            @PathVariable("challengeId") Long challengeId) throws IOException {

        for(MultipartFile multipartFile : certifyExampleImages){
            log.info("contentType ::::::::: " + multipartFile.getContentType());
        }
        imageService.uploadCertifyExampleImages(certifyExampleImages, challengeId, "SUCCESS");
        return ResponseEntity.ok(CommonResponse.from("인증 성공 예시 이미지가 업로드 되었습니다."));
    }

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 인증 실패 예시 이미지 업로드 api")
    @PostMapping(value="/images/failure/{challengeId}")
    public ResponseEntity<CommonResponse> createFailureExampleImages(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                           @RequestPart(value = "certifyExampleImages") List<MultipartFile> certifyExampleImages,
                                                           @PathVariable("challengeId") Long challengeId) throws IOException {

        for(MultipartFile multipartFile : certifyExampleImages){
            log.info("contentType ::::::::: " + multipartFile.getContentType());
        }
        imageService.uploadCertifyExampleImages(certifyExampleImages, challengeId, "FAILURE");
        return ResponseEntity.ok(CommonResponse.from("인증 실패 예시 이미지가 업로드 되었습니다."));
    }

}
