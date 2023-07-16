package com.cmc.oauth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenResDto {

    private String tokenType;

    private String accessToken;

    private String idToken;

    private Long expiresIn;

    private String refreshToken;

    private Long refreshTokenExpiresIn;

    private String scope;

    public static KakaoTokenResDto fail() {
        return new KakaoTokenResDto(null, null);
    }

    private KakaoTokenResDto(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
