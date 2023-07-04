package com.cmc.oauth.controller;

import com.cmc.common.exception.BadRequestException;
import com.cmc.oauth.constant.ClientType;
import com.cmc.oauth.constant.SocialType;
import com.cmc.oauth.dto.request.OauthReqDto;
import com.cmc.oauth.dto.response.ResponseJwtTokenDto;
import com.cmc.oauth.service.KakaoLoginService;
import com.cmc.oauth.service.OauthLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "oauth", description = "oauth API")
@RequestMapping("/api/oauth")
public class OauthLoginController {

    private final OauthLoginService oauthLoginService;
    private final KakaoLoginService kakaoLoginService;

    @Tag(name = "oauth")
    @PostMapping(value = "/login", headers = {"Content-type=application/json"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "OAuth 로그인 API", description = "Authorization code로 로그인 시 JWT 토큰 반환")
    public ResponseEntity<ResponseJwtTokenDto> loginOauth(@Valid @RequestBody OauthReqDto oauthReqDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        log.info("=== Oauth login start ===");

        final SocialType socialType = oauthReqDto.getSocialType();
        ResponseJwtTokenDto jwtTokenDto;

        if (socialType == SocialType.KAKAO && oauthReqDto.getClientType() == ClientType.ANDROID) {

            kakaoLoginService.getInfo(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).getKakaoUserInfoResDto();

        } else if (socialType == SocialType.KAKAO && oauthReqDto.getClientType() == ClientType.IOS) {
            final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (tokenString == null || tokenString.isEmpty()) {
                throw new BadRequestException("토큰이 없습니다.");
            }

        } else if (socialType == SocialType.APPLE && oauthReqDto.getClientType() == ClientType.IOS) {
            final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (tokenString == null || tokenString.isEmpty()) {
                throw new BadRequestException("토큰이 없습니다.");
            }

            jwtTokenDto = oauthLoginService.loginAppleIos(tokenString);

        } else {
            throw new BadRequestException("클라이언트 타입이 올바르지 않습니다.");
        }

        log.info("=== Oauth login end ===");
        //return ResponseEntity.ok(jwtTokenDto);
        return null;
    }

}
