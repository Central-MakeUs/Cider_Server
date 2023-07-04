package com.cmc.oauth.dto.request;

import com.cmc.oauth.constant.ClientType;
import com.cmc.oauth.constant.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class OauthReqDto {

    @Schema(description = "소셜 로그인 타입 (KAKAO, APPLE)")
    private SocialType socialType = SocialType.KAKAO;

    @Schema(description = "로그인 클라이언트 타입")
    private ClientType clientType = ClientType.ANDROID;
}

