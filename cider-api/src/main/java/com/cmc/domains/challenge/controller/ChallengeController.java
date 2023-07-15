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
import org.springframework.http.MediaType;
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
        return ResponseEntity.ok(ChallengeCreateResponseDto.create(challenge));
    }

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 인증 예시 이미지 업로드 api", description = "- form-data 형태로 보내주시고, content-type는 따로 지정 안해주셔도 됩니다.\n" +
            "- challengeId는 직전에 호출한 챌린지 생성 api response값 보내주시면 됩니다.")
    @PostMapping(value="/images/{challengeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> createSuccessExampleImages(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                     @RequestPart(value = "successExampleImages") List<MultipartFile> successExampleImages,
                                                                     @RequestPart(value = "failureExampleImages") List<MultipartFile> failureExampleImages,
                                                                     @PathVariable("challengeId") Long challengeId) throws IOException {

        for(MultipartFile multipartFile : successExampleImages){
            log.info("contentType ::::::::: " + multipartFile.getContentType());
        }
        imageService.uploadCertifyExampleImages(successExampleImages, challengeId, memberId, "SUCCESS");
        imageService.uploadCertifyExampleImages(failureExampleImages, challengeId, memberId, "FAILURE");
        return ResponseEntity.ok(CommonResponse.from("인증 예시 이미지가 업로드 되었습니다."));
    }

}
