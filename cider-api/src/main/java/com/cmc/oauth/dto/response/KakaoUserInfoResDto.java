package com.cmc.oauth.dto.response;

import com.cmc.oauth.dto.response.userInfo.Profile;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoUserInfoResDto {

    private Profile profile;
    private String gender;
    private String birthday;
    private String email;
}
