package com.cmc.domains.challenge.dto.request;

import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Data
public class ChallengeCreateRequestDto {

    @Schema(description = "챌린지 개설 분야", example = "T: 재테크, M: 돈관리, L: 금융학습, C: 소비절약 ", required=true)
    private String challengeBranch;

    @Schema(description = "챌린지 제목", example = "매일 작업하기", required=true)
    private String challengeName;

    @Schema(description = "챌린지 소개", example = "챌린지 소개~~", required=true)
    private String challengeInfo;

    @Schema(description = "참여 정원", example = "15", required=true)
    private Integer challengeCapacity;

    @Schema(description = "모집 기간", example = "7 (일 단위)", required=true)
    private Integer recruitPeriod;

    @Schema(description = "챌린지 기간", example = "8 (주 단위)", required=true)
    private Integer challengePeriod;

    @Schema(description = "챌린지 공개 여부", example = "true: 공개, fale: 비공개", required=true)
    private Boolean isPublic;

//    @Schema(description = "성공 예시 사진 리스트 (1-2개)", required=true)
//    List<MultipartFile> successImages;

//    @Schema(description = "실패 예시 사진 리스트 (1-2개)", required=true)
//    List<MultipartFile> failureImages;

    public Challenge toEntity(){
        return Challenge.builder()
                .challengeBranch(challengeBranch)
                .challengeName(challengeName)
                .challengeInfo(challengeInfo)
                .challengeCapacity(challengeCapacity)
                .recruitPeriod(recruitPeriod)
                .challengePeriod(challengePeriod)
                .isPublic(isPublic)
                .build();
    }

}
