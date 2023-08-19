package com.cmc.domains.certify.controller;

import com.cmc.block.Block;
import com.cmc.block.constant.BlockType;
import com.cmc.certify.Certify;
import com.cmc.certifyLike.CertifyLike;
import com.cmc.challenge.Challenge;
import com.cmc.challenge.constant.ChallengeStatus;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.response.CommonResponse;
import com.cmc.domains.certify.dto.request.CertifyCreateRequestDto;
import com.cmc.domains.certify.dto.request.CertifyLikeCreateRequestDto;
import com.cmc.domains.certify.dto.response.*;
import com.cmc.domains.certify.service.CertifyService;
import com.cmc.domains.challenge.dto.response.ChallengeResponseDto;
import com.cmc.domains.challenge.dto.response.SimpleChallengeResponseDto;
import com.cmc.domains.challenge.service.ChallengeService;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.cmc.domains.challengeLike.dto.request.ChallengeLikeCreateRequestDto;
import com.cmc.domains.image.service.ImageService;
import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import com.cmc.domains.member.service.MemberService;
import com.cmc.global.resolver.RequestMemberId;
import com.cmc.member.Member;
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
    private final MemberService memberService;
    private final ChallengeService challengeService;

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

                Long memberId = TokenProvider.getMemberIdKakao(tokenString);
                Member member = memberService.find(memberId);

                if(!checkIsBlocked(member, certify)) {
                    return CertifyResponseDto.from(certify, findIsLike(certify, memberId));
                }else {
                    return null;
                }
            }).collect(Collectors.toList());
        }
        return ResponseEntity.ok(certifyResponseDtos);
    }

    private Boolean checkIsBlocked(Member member, Certify certify){

        for (Block block : member.getBlocks()){
            if (block.getBlockType().equals(BlockType.FEED) && block.getCertify().equals(certify)){
                return true;
            }
            else if (block.getBlockType().equals(BlockType.MEMBER) && block.getMember().equals(member)){
                return true;
            }
        }
        return false;
    }

    @Tag(name = "myPage", description = "마이페이지 API")
    @Operation(summary = "마이페이지 - 나의 인증글 조회 api")
    @GetMapping("/mypage/{challengeId}")
    public ResponseEntity<MyCertifyListResponseDto> getMyCertifyListV2(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                       @PathVariable("challengeId") Long challengeId) {

        Member member = memberService.find(memberId);
        Challenge challenge = challengeService.getChallenge(challengeId);
        List<Certify> certifies = certifyService.getMyCertifyList(memberId, challengeId);
        List<SimpleCertifyResponseDtoV2> certifyResponseList = certifies.stream().map(certify -> {
            return SimpleCertifyResponseDtoV2.from(certify, findIsLike(certify, memberId));
        }).toList();

        MyCertifyListResponseDto result = MyCertifyListResponseDto.from(SimpleMemberResponseDto.from(member),
                SimpleChallengeResponseDto.from(challenge), certifyResponseList);

        return ResponseEntity.ok(result);
    }

    private Boolean findIsLike(Certify certify, Long memberId){

        for(CertifyLike certifyLike : certify.getCertifyLikeList()){
            if (certifyLike.getMember().getMemberId().equals(memberId)){
                return true;
            }
        }
        return false;
    }

}
