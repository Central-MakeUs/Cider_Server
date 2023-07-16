package com.cmc.oauth.dto.response.userInfo;

import com.cmc.oauth.dto.response.KakaoAccount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import netscape.javascript.JSObject;

import java.util.Date;

@ToString
@Getter
@NoArgsConstructor
@JsonIgnoreProperties("properties")
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoInfoResDto {

    private Long id;

    private Date connectedAt;

    private Date synchedAt;

    // private JsonObject properties;

    private KakaoAccount kakaoAccount;

    public static KakaoInfoResDto fail() {
        return null;
    }
}
