package com.cmc.oauth.dto.response.userInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties({"thumbnail_image_url", "is_default_image"})
public class Profile {

    private String nickname;

    private String profile_image_url;
}
