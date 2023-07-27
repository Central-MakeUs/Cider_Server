package com.cmc.domains.certify.dto.response;

import com.cmc.certify.Certify;
import com.cmc.domains.challenge.dto.response.SimpleChallengeResponseDto;
import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CertifyResponseDto {

    @Schema(description = "간단 멤버 정보")
    private SimpleMemberResponseDto simpleMemberResponseDto;

    @Schema(description = "간단 챌린지 정보")
    private SimpleChallengeResponseDto simpleChallengeResponseDto;

    @Schema(description = "인증글 생성일", example = "2023-07-27T14:27")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "인증글 제목", example = "오늘도 인증해요")
    private String certifyName;

    @Schema(description = "인증글 내용", example = "커피값을 아꼈어요")
    private String certifyContent;

    @Schema(description = "인증글 좋아요수", example = "200")
    private Long certifyLike;

    @Schema(description = "로그인 한 사용자 - 인증글 좋아요 여부", example = "false")
    private Boolean isLike;

    public static CertifyResponseDto from(Certify certify) {

        return CertifyResponseDto.builder()
                .simpleMemberResponseDto(SimpleMemberResponseDto.from(certify.getParticipate().getMember()))
                .simpleChallengeResponseDto(SimpleChallengeResponseDto.from(certify.getParticipate().getChallenge()))
                .createdDate(certify.getCreatedDate())
                .certifyName(certify.getCertifyName())
                .certifyContent(certify.getCertifyContent())
                .certifyLike((long) certify.getCertifyLikeList().size())
                .isLike(false)
                .build();
    }

    public static CertifyResponseDto from(Certify certify, Boolean isLike) {

        return CertifyResponseDto.builder()
                .simpleMemberResponseDto(SimpleMemberResponseDto.from(certify.getParticipate().getMember()))
                .simpleChallengeResponseDto(SimpleChallengeResponseDto.from(certify.getParticipate().getChallenge()))
                .createdDate(certify.getCreatedDate())
                .certifyName(certify.getCertifyName())
                .certifyContent(certify.getCertifyContent())
                .certifyLike((long) certify.getCertifyLikeList().size())
                .isLike(isLike)
                .build();
    }


}
