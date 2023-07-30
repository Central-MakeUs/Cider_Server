package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.format.TextStyle;
import java.util.Locale;

@Data
@Builder
@AllArgsConstructor
public class ChallengeInfoResponseDto {

    private String recruitPeriod;

    private String challengePeriod;

    private Integer challengeCapacity;

    private Integer certifyNum;

    private String certifyTime;

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
