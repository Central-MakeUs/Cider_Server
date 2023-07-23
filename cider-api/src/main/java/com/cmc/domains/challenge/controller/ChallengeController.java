package com.cmc.domains.challenge.controller;

import com.cmc.challenge.Challenge;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.exception.BadRequestException;
import com.cmc.common.response.CommonResponse;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.dto.request.ChallengeParticipateRequestDto;
import com.cmc.domains.challenge.dto.response.ChallengeCreateResponseDto;
import com.cmc.domains.challenge.dto.response.ChallengeHomeResponseDto;
import com.cmc.domains.challenge.dto.response.ChallengeResponseDto;
import com.cmc.domains.challenge.service.ChallengeService;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.cmc.domains.image.service.ImageService;
import com.cmc.domains.participate.service.ParticipateService;
import com.cmc.global.resolver.RequestMemberId;
import com.cmc.oauth.service.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "challenge", description = "챌린지 API")
@RequestMapping("/api/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ImageService imageService;
    private final ParticipateService participateService;

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 생성 api")
    @PostMapping(value="")
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                    @RequestBody @Valid ChallengeCreateRequestDto req){

        Challenge challenge = challengeService.create(req, memberId);
        participateService.create(challenge.getChallengeId(), memberId);
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

    @Tag(name = "challenge")
    @Operation(summary = "홈 - 인기 챌린지, 공식 챌린지 조회 api")
    @GetMapping("/home")
    public ResponseEntity<ChallengeHomeResponseDto> getChallengeHome(HttpServletRequest httpServletRequest) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        // 인기 챌린지
        List<ChallengeResponseVo> popularChallengeVos = challengeService.getPopularChallenges();
        List<ChallengeResponseDto> popularChallengeResponseDtos = makeChallengeResponseDto(tokenString, popularChallengeVos);

        // 공식 챌린지
        List<ChallengeResponseVo> officialChallengeVos = challengeService.getOfficialChallenges();
        List<ChallengeResponseDto> officialChallengeResponseDtos = makeChallengeResponseDto(tokenString, officialChallengeVos);

        return ResponseEntity.ok(ChallengeHomeResponseDto.from(popularChallengeResponseDtos, officialChallengeResponseDtos));
    }

    @Tag(name = "challenge")
    @Operation(summary = "홈 - 카테고리 별 챌린지 조회 api")
    @GetMapping("/home/{category}")
    public ResponseEntity<List<ChallengeResponseDto>> getChallengeHomeCategory(HttpServletRequest httpServletRequest,
                                                                               @PathVariable("category") String category) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        List<ChallengeResponseVo> challengeVos = challengeService.getCategoryChallenges(category);
        List<ChallengeResponseDto> challengeResponseDtos = makeChallengeResponseDto(tokenString, challengeVos);
        return ResponseEntity.ok(challengeResponseDtos);
    }

    private List<ChallengeResponseDto> makeChallengeResponseDto(String tokenString, List<ChallengeResponseVo> challengeVos){

        List<ChallengeResponseDto> challengeResponseDtos = new ArrayList<>();
        if (tokenString == null || tokenString.isEmpty()) {
            // 로그인 x
            challengeResponseDtos = challengeVos.stream().map(vo -> {
                return ChallengeResponseDto.from(vo.getChallenge(), vo.getParticipateNum(),
                        ChronoUnit.DAYS.between(LocalDate.now(), vo.getChallenge().getChallengeStartDate()));
            }).toList();
        } else{
            // 로그인 o
            challengeResponseDtos = challengeVos.stream().map(vo -> {
                return ChallengeResponseDto.from(vo.getChallenge(), vo.getParticipateNum(),
                        findIsLike(vo.getChallenge(), TokenProvider.getMemberId(tokenString)), ChronoUnit.DAYS.between(LocalDate.now(), vo.getChallenge().getChallengeStartDate()));
            }).toList();
        }

        return challengeResponseDtos;
    }

    private Boolean findIsLike(Challenge challenge, Long memberId){

        for(ChallengeLike challengeLike : challenge.getChallengeLikes()){
            if (challengeLike.getMember().getMemberId().equals(memberId)){
                return true;
            }
        }
        return false;
    }

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 참여하기 api")
    @PostMapping(value="/participate")
    public ResponseEntity<CommonResponse> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                      @RequestBody @Valid ChallengeParticipateRequestDto req){

        participateService.create(req.getChallengeId(), memberId);
        return ResponseEntity.ok(CommonResponse.from("챌린지 참여가 완료되었습니다"));
    }
}
