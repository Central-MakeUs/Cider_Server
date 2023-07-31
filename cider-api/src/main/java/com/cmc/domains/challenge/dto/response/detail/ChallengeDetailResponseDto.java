package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.internal.bytebuddy.build.Plugin;

@Data
@Builder
@AllArgsConstructor
public class ChallengeDetailResponseDto {

    private Long challengeId;

    private String myChallengeStatus;

    private String challengeBranch;

    private String challengeName;

    private Long challengeCapacity;

    private Long participateNum;

    private String challengeStatus;

    private String challengeIntro;

    private Long challengeLikeNum;

    private Boolean isLike;

    private ChallengeConditionResponseDto challengeConditionResponseDto;

    private ChallengeInfoResponseDto challengeInfoResponseDto;

    private ChallengeRuleResponseDto challengeRuleResponseDto;

    private CertifyMissionResponseDto certifyMissionResponseDto;

    private SimpleMemberResponseDto simpleMemberResponseDto;

    public static ChallengeDetailResponseDto from(Challenge challenge, String myChallengeStatus, Boolean isLike,ChallengeConditionResponseDto challengeConditionResponseDto, ChallengeInfoResponseDto challengeInfoResponseDto,
                                                  ChallengeRuleResponseDto challengeRuleResponseDto, CertifyMissionResponseDto certifyMissionResponseDto, SimpleMemberResponseDto simpleMemberResponseDto){

        return new ChallengeDetailResponseDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .myChallengeStatus(myChallengeStatus)
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .challengeCapacity(Long.valueOf(challenge.getChallengeCapacity()))
                .participateNum((long) challenge.getParticipates().size())
                .challengeStatus(String.valueOf(challenge.getChallengeStatus()))
                .challengeIntro(challenge.getChallengeInfo())
                .challengeLikeNum((long) challenge.getChallengeLikes().size())
                .isLike(isLike)
                .challengeConditionResponseDto(challengeConditionResponseDto)
                .challengeInfoResponseDto(challengeInfoResponseDto)
                .challengeRuleResponseDto(challengeRuleResponseDto)
                .certifyMissionResponseDto(certifyMissionResponseDto)
                .simpleMemberResponseDto(simpleMemberResponseDto)
                .build();

    }

    public static ChallengeDetailResponseDto from(Challenge challenge, String myChallengeStatus, ChallengeConditionResponseDto challengeConditionResponseDto, ChallengeInfoResponseDto challengeInfoResponseDto,
                                                  ChallengeRuleResponseDto challengeRuleResponseDto, CertifyMissionResponseDto certifyMissionResponseDto, SimpleMemberResponseDto simpleMemberResponseDto){

        return new ChallengeDetailResponseDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .myChallengeStatus(myChallengeStatus)
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .challengeCapacity(Long.valueOf(challenge.getChallengeCapacity()))
                .participateNum((long) challenge.getParticipates().size())
                .challengeStatus(String.valueOf(challenge.getChallengeStatus()))
                .challengeIntro(challenge.getChallengeInfo())
                .challengeLikeNum((long) challenge.getChallengeLikes().size())
                .isLike(false)
                .challengeConditionResponseDto(challengeConditionResponseDto)
                .challengeInfoResponseDto(challengeInfoResponseDto)
                .challengeRuleResponseDto(challengeRuleResponseDto)
                .certifyMissionResponseDto(certifyMissionResponseDto)
                .simpleMemberResponseDto(simpleMemberResponseDto)
                .build();

    }
}
