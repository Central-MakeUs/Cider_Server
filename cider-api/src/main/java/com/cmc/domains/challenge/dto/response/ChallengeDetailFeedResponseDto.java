package com.cmc.domains.challenge.dto.response;

import com.cmc.challenge.Challenge;
import com.cmc.domains.certify.dto.response.CertifyResponseDto;
import com.cmc.domains.certify.dto.response.SimpleCertifyResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChallengeDetailFeedResponseDto {

    @Schema(description = "챌린지 id", example = "10")
    private Long challengeId;

    @Schema(description = "챌린지 분야", example = "TECHNOLOGY: 재태크, SAVING: 소비절약, LEARNING: 금융학습, MONEY: 돈관리")
    private String challengeBranch;

    @Schema(description = "챌린지 제목", example = "일단 냉장고 파먹기")
    private String challengeName;

    @Schema(description = "챌린지 모집 인원", example = "30")
    private Integer challengeCapacity;

    @Schema(description = "챌린지 참여 인원", example = "29")
    private Integer participateNum;

    @Schema(description = "활동 한눈에 보기 - 이미지 url", example = "List<String> urlList")
    List<String> certifyImageUrlList;

    @Schema(description = "인증글 리스트")
    List<SimpleCertifyResponseDto> simpleCertifyResponseDtoList;

    public static ChallengeDetailFeedResponseDto from(Challenge challenge, List<String> certifyImageUrlList, List<SimpleCertifyResponseDto> certifyResponseDtos){

        return new ChallengeDetailFeedResponseDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .challengeBranch(String.valueOf(challenge.getChallengeBranch()))
                .challengeName(challenge.getChallengeName())
                .challengeCapacity(challenge.getChallengeCapacity())
                .participateNum(challenge.getParticipates().size())
                .certifyImageUrlList(certifyImageUrlList)
                .simpleCertifyResponseDtoList(certifyResponseDtos)
                .build();
    }
}
