package com.cmc.oauth.service;

import com.cmc.global.config.KakaoFeignConfiguration;
import com.cmc.oauth.dto.response.KakaoTokenResDto;
import com.cmc.oauth.dto.response.userInfo.KakaoInfoResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Component
@FeignClient(name = "KakaoFeignService", configuration = KakaoFeignConfiguration.class)
public interface KakaoFeignService {

    @PostMapping
    KakaoInfoResDto getInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

    @PostMapping
    KakaoTokenResDto getToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                              @RequestParam("redirect_uri") String redirectUrl,
                              @RequestParam("code") String code,
                              @RequestParam("grant_type") String grantType);

}
