package com.cmc.oauth.dto;

import com.cmc.oauth.constant.SocialType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthLoginDto {

    private String accessToken;

    private SocialType socialType;
}
