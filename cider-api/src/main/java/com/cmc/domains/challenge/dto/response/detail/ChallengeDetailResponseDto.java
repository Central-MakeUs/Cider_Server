package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class ChallengeDetailResponseDto {

    @Schema(description = "챌린지 id", example = "10")
    private Long challengeId;

    @Schema(description = "하단 버튼 문구 ", example = "이 챌린지 참여하기 / 오늘 참여 인증하기")
    private String myChallengeStatus;

    @Schema(description = "챌린지 분야", example = "TECHNOLOGY: 재태크, SAVING: 소비절약, LEARNING: 금융학습, MONEY: 돈관리")
    private String challengeBranch;

    @Schema(description = "챌린지 제목", example = "일단 냉장고 파먹기")
    private String challengeName;

    @Schema(description = "챌린지 모집 인원", example = "30")
    private Integer challengeCapacity;

    @Schema(description = "챌린지 참여 인원", example = "29")
    private Integer participateNum;

    @Schema(description = "챌린지 상태", example = "RECRUITING: 모집중, POSSIBLE: 참여 가능, IMPOSSIBLE: 참여 불가(모집 종료)")
    private String challengeStatus;

    @Schema(description = "챌린지 소개", example = "하루 한번 아침에 일어나서 물이 담긴 컵사진 인증 ...~")
    private String challengeIntro;

    @Schema(description = "챌린지 좋아요 개수", example = "346")
    private Long challengeLikeNum;

    @Schema(description = "내 좋아요 여부", example = "true/false")
    private Boolean isLike;

    @Schema(description = "챌린지 현황")
    private ChallengeConditionResponseDto challengeConditionResponseDto;

    @Schema(description = "챌린지 정보")
    private ChallengeInfoResponseDto challengeInfoResponseDto;

    @Schema(description = "챌린지 규칙")
    private ChallengeRuleResponseDto challengeRuleResponseDto;

    @Schema(description = "인증 미션")
    private CertifyMissionResponseDto certifyMissionResponseDto;

    @Schema(description = "챌린지 호스트")
    private SimpleMemberResponseDto simpleMemberResponseDto;

    public static ChallengeDetailResponseDto from(Challenge challenge, String myChallengeStatus, Boolean isLike,ChallengeConditionResponseDto challengeConditionResponseDto, ChallengeInfoResponseDto challengeInfoResponseDto,
                                                  ChallengeRuleResponseDto challengeRuleResponseDto, CertifyMissionResponseDto certifyMissionResponseDto, SimpleMemberResponseDto simpleMemberResponseDto){

        return new ChallengeDetailResponseDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .challengeName(challenge.getChallengeName())
                .myChallengeStatus(myChallengeStatus)
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .challengeCapacity(challenge.getChallengeCapacity())
                .participateNum(challenge.getParticipates().size())
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
                .challengeName(challenge.getChallengeName())
                .myChallengeStatus(myChallengeStatus)
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .challengeCapacity(challenge.getChallengeCapacity())
                .participateNum(challenge.getParticipates().size())
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
