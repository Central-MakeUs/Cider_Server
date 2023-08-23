package com.cmc.oauth.controller;

import com.cmc.challenge.constant.ChallengeStatus;
import com.cmc.common.exception.BadRequestException;
import com.cmc.domains.challenge.service.ChallengeService;
import com.cmc.domains.member.service.MemberService;
import com.cmc.domains.participate.service.ParticipateService;
import com.cmc.global.resolver.RequestMemberId;
import com.cmc.member.Member;
import com.cmc.oauth.constant.SocialType;
import com.cmc.oauth.dto.request.OauthReqDto;
import com.cmc.oauth.dto.response.KakaoAccount;
import com.cmc.oauth.dto.response.ResponseJwtTokenDto;
import com.cmc.oauth.service.KakaoLoginService;
import com.cmc.oauth.service.OauthLoginService;
import com.cmc.participate.Participate;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.time.LocalDateTime;

import static com.cmc.member.QMember.member;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "oauth", description = "oauth API")
@RequestMapping("/api/oauth")
public class OauthLoginController {

    private final OauthLoginService oauthLoginService;
    private final KakaoLoginService kakaoLoginService;
    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final ParticipateService participateService;

    @Tag(name = "oauth")
    @PostMapping(value = "/login", headers = {"Content-type=application/json"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "OAuth 로그인 API", description = "Authorization code로 로그인 시 JWT 토큰 반환")
    public ResponseEntity<ResponseJwtTokenDto> loginOauth(@Valid @RequestBody OauthReqDto oauthReqDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        log.info("=== Oauth login start ===");

        log.info("authorization :::::: " + httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
        log.info("socialType ::::: " + oauthReqDto.getSocialType());
        log.info("clientType :::: " + oauthReqDto.getClientType());

        final SocialType socialType = oauthReqDto.getSocialType();
        ResponseJwtTokenDto jwtTokenDto;

        if (socialType == SocialType.KAKAO) {

            final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (tokenString == null || tokenString.isEmpty()) {
                throw new BadRequestException("토큰이 없습니다.");
            }

            KakaoAccount memberInfo = kakaoLoginService.getInfoV2(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).getKakaoAccount();

            jwtTokenDto = oauthLoginService.createMemberAndJwt(memberInfo, socialType);

        } else if (socialType == SocialType.APPLE) {

            final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (tokenString == null || tokenString.isEmpty()) {
                throw new BadRequestException("토큰이 없습니다.");
            }

            jwtTokenDto = oauthLoginService.loginAppleIos(tokenString);

        } else {
            throw new BadRequestException("클라이언트 타입이 올바르지 않습니다.");
        }

        log.info("=== Oauth login end ===");
        return ResponseEntity.ok(jwtTokenDto);
    }

    @Tag(name = "oauth")
    @PostMapping(value = "/logout")
    @Operation(summary = "로그아웃", description = "- refresh token으로만 요청 가능, 로그아웃 처리 시 db에 저장된 refresh token 만료 처리\n" + "- 헤더의 Authorization 값으로 Refresh token 넣어 요청주세요.")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization") String refreshToken) {
        oauthLoginService.logout(refreshToken, LocalDateTime.now());
        return ResponseEntity.ok().body("로그아웃이 완료되었습니다.");
    }

    @Tag(name = "oauth")
    @PostMapping(value = "/signout")
    @Operation(summary = "회원 탈퇴", description = "토큰에 해당하는 멤버의 refresh token 만료 처리")
    public ResponseEntity<String> signOut(@Parameter(hidden = true) @RequestMemberId Long memberId) {

        // 개설 챌린지 호스트 관리자로 업데이트
        Member admin = memberService.getAdmin();
        Member member = memberService.find(memberId);
        for(Participate participate : member.getParticipates()){
            if (participate.getIsCreator()){
                participateService.createFirst(participate.getChallenge().getChallengeId(), admin.getMemberId());
            }
        }

        // 탈퇴
        oauthLoginService.signOut(memberId);
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }

}
