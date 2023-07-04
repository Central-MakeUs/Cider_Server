package com.cmc.oauth.dto.response.userInfo;

import com.cmc.oauth.dto.response.KakaoUserInfoResDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoInfoResDto {

    private KakaoUserInfoResDto kakaoUserInfoResDto;

    public static KakaoInfoResDto fail() {
        return null;
    }
}
