package com.cmc.oauth.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.util.Date;

@Getter @Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class ResponseJwtTokenDto {

    @Schema(description = "access token")
    private String accessToken;

    @Schema(description = "access token 만료 시간")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date accessTokenExpireTime;

    @Schema(description = "refresh token")
    private String refreshToken;

    @Schema(description = "refresh token 만료 시간")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date refreshTokenExpireTime;

    @Schema(description = "신규 회원 여부 - 이름이 저장되어 있는지 여부")
    @Builder.Default
    private Boolean isNewMember = false;

    @Schema(description = "멤버 ID")
    private Long memberId;

    @Schema(description = "멤버 이름")
    private String memberName;

    @Schema(description = "멤버 생일")
    private String birthday;

    @Schema(description = "멤버 성별")
    private String gender;

    @Schema(description = "멤버 업데이트 api 실행 여부", example = "실행 전적 있으면 true")
    private Boolean isUpdatedMember;

}