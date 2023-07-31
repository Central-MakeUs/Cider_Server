package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.format.TextStyle;
import java.util.Locale;

@Data
@Builder
@AllArgsConstructor
public class ChallengeInfoResponseDto {

    @Schema(description = "모집 기간", example = "06월 6일(금) ~ 06월 24일(토)")
    private String recruitPeriod;

    @Schema(description = "챌린지 기간", example = "06월 6일(금) ~ 06월 24일(토)")
    private String challengePeriod;

    @Schema(description = "모집 인원", example = "30")
    private Integer challengeCapacity;

    @Schema(description = "인증 횟수", example = "20")
    private Integer certifyNum;

    @Schema(description = "인증 시간", example = "매일 자정까지")
    private String certifyTime;

    @Schema(description = "리워드 여부", example = "true/false")
    private Boolean isReward;

    public static ChallengeInfoResponseDto from(Challenge challenge){

        return new ChallengeInfoResponseDtoBuilder()
                .recruitPeriod(challenge.getRecruitStartDate().getMonthValue() + "월 " + challenge.getRecruitStartDate().getDayOfMonth() + "일("
                        + challenge.getRecruitStartDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN) + ") ~ "
                        + challenge.getRecruitEndDate().getMonthValue() + "월 " + challenge.getRecruitEndDate().getDayOfMonth() + "일("
                        + challenge.getRecruitEndDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN))
                .challengePeriod(challenge.getChallengeStartDate().getMonthValue() + "월 " + challenge.getChallengeStartDate().getDayOfMonth() + "일("
                    + challenge.getChallengeStartDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN) + ") ~ "
                    + challenge.getChallengeEndDate().getMonthValue() + "월 " + challenge.getChallengeEndDate().getDayOfMonth() + "일("
                    + challenge.getChallengeEndDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN))
                .challengeCapacity(challenge.getChallengeCapacity())
                .certifyNum(challenge.getCertifyNum())
                .certifyTime("매일 자정까지")
                .isReward(challenge.getIsReward())
                .build();
    }
}
