package com.cmc.domains.certify.controller;

import com.cmc.certify.Certify;
import com.cmc.certifyLike.CertifyLike;
import com.cmc.challenge.Challenge;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.response.CommonResponse;
import com.cmc.domains.certify.dto.request.CertifyCreateRequestDto;
import com.cmc.domains.certify.dto.request.CertifyLikeCreateRequestDto;
import com.cmc.domains.certify.dto.response.CertifyCreateResponseDto;
import com.cmc.domains.certify.dto.response.CertifyResponseDto;
import com.cmc.domains.certify.service.CertifyService;
import com.cmc.domains.challenge.dto.response.ChallengeResponseDto;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.cmc.domains.challengeLike.dto.request.ChallengeLikeCreateRequestDto;
import com.cmc.domains.image.service.ImageService;
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
@RequestMapping("/api/certify")
public class CertifyController {

    private final CertifyService certifyService;
    private final ImageService imageService;

    @Tag(name = "certify", description = "챌린지 인증 API")
    @Operation(summary = "챌린지 인증 api")
    @PostMapping(value="")
    public ResponseEntity<CertifyCreateResponseDto> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                      @RequestBody @Valid CertifyCreateRequestDto req){

        Certify certify = certifyService.create(req.getChallengeId(), req.getCertifyName(), req.getCertifyContent(), memberId);
        return ResponseEntity.ok(CertifyCreateResponseDto.create(certify));
    }

    @Tag(name = "certify", description = "챌린지 인증 API")
    @Operation(summary = "챌린지 인증 이미지 업로드 api", description = "- form-data 형태로 보내주시고, content-type는 따로 지정 안해주셔도 됩니다.\n" +
            "- certifyId는 직전에 호출한 챌린지 인증 api response값 보내주시면 됩니다.")
    @PostMapping(value="/images/{certifyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> createCertifyImages(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                     @RequestPart(value = "certifyImages") List<MultipartFile> certifyImages,
                                                                     @PathVariable("certifyId") Long certifyId) throws IOException {

        for(MultipartFile multipartFile : certifyImages){
            log.info("contentType ::::::::: " + multipartFile.getContentType());
        }
        imageService.uploadCertifyImages(certifyImages, certifyId);
        return ResponseEntity.ok(CommonResponse.from("인증 예시 이미지가 업로드 되었습니다."));
    }

    @Tag(name = "home", description = "홈(둘러보기) API")
    @Operation(summary = "홈 - 추천 피드 조회 api")
    @GetMapping("/home")
    public ResponseEntity<List<CertifyResponseDto>> getPopularChallengeList(HttpServletRequest httpServletRequest) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        List<Certify> certifies = certifyService.getCertifyList();

        List<CertifyResponseDto> certifyResponseDtos = new ArrayList<>();
        if (tokenString == null || tokenString.isEmpty()) {
            // 로그인 x
            certifyResponseDtos = certifies.stream().map(CertifyResponseDto::from).collect(Collectors.toList());
        } else{
            // 로그인 o
            certifyResponseDtos = certifies.stream().map(certify -> {
                return CertifyResponseDto.from(certify, findIsLike(certify,  TokenProvider.getMemberIdKakao(tokenString)));
            }).collect(Collectors.toList());
        }
        return ResponseEntity.ok(certifyResponseDtos);
    }

    private Boolean findIsLike(Certify certify, Long memberId){

        for(CertifyLike certifyLike : certify.getCertifyLikeList()){
            if (certifyLike.getMember().getMemberId().equals(memberId)){
                return true;
            }
        }
        return false;
    }

    @Tag(name = "certifyLike", description = "인증 좋아요 API")
    @Operation(summary = "인증 좋아요 등록 api")
    @PostMapping(value="/like")
    public ResponseEntity<CommonResponse> createCertifyLike(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                              @RequestBody @Valid CertifyLikeCreateRequestDto req){

        certifyService.createLike(memberId, req.getCertifyId());
        return ResponseEntity.ok(CommonResponse.from("인증글 좋아요가 등록되었습니다"));
    }

    @Tag(name = "certifyLike", description = "인증 좋아요 API")
    @Operation(summary = "인증 좋아요 삭제 api")
    @DeleteMapping(value="/like/{certifyId}")
    public ResponseEntity<CommonResponse> deleteCertifyLike(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                              @PathVariable("certifyId") Long certifyId) {

        certifyService.deleteLike(memberId, certifyId);
        return ResponseEntity.ok(CommonResponse.from("인증글 좋아요가 삭제되었습니다"));
    }

}