package com.cmc.oauth.dto.response;

import com.cmc.oauth.dto.response.userInfo.Profile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties({"is_email_verified", "age_range_needs_agreement", "age_range", "birthday_type", "phone_number_needs_agreement", "phone_number", "ci_needs_agreement", "has_email", "ci", "ci_authenticated_at"})
public class KakaoAccount {

    private Profile profile;

    private String name;

    private String email;

    private String birthyear;

    private String birthday;

    private String gender;

    private Boolean profile_needs_agreement;

    private Boolean profile_nickname_needs_agreement;

    private Boolean profile_image_needs_agreement;

    private Boolean name_needs_agreement;

    private Boolean email_needs_agreement;

    private Boolean is_email_valid;

    private Boolean birthyear_needs_agreement;

    private Boolean gender_needs_agreement;
}
