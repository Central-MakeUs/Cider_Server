package com.cmc.oauth.service;

import com.cmc.oauth.dto.response.KakaoTokenResDto;
import com.cmc.oauth.dto.response.KakaoUserInfoResDto;
import com.cmc.oauth.dto.response.userInfo.KakaoInfoResDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoFeignService kakaoFeignService;

    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.user-api-url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    public KakaoInfoResDto getInfo(final String code) {

        // code <-> accessToken 교환
        final KakaoTokenResDto token = getToken(code);
        log.debug("token = {}", token);

        try {
            return kakaoFeignService.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());
        } catch (Exception e) {
            log.error("something error..", e);
            return KakaoInfoResDto.fail();
        }
    }

    private KakaoTokenResDto getToken(final String code) {
        try {
            return kakaoFeignService.getToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.error("Something error..", e);
            return KakaoTokenResDto.fail();
        }
    }
}
