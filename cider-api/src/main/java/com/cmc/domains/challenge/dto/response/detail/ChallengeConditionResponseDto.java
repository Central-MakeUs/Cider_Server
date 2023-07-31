package com.cmc.domains.challenge.dto.response.detail;

import com.cmc.challenge.Challenge;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@AllArgsConstructor
public class ChallengeConditionResponseDto {

    @Schema(description = "챌린지 기간", example = "'30회 중 6회 진행의 30")
    private Integer challengePeriod;

    @Schema(description = "챌린지 진행일", example = "30회 중 6회 진행의 6")
    private Long ongoingDate;

    @Schema(description = "평균 현황", example = "% 기준")
    private Integer averageCondition;

    @Schema(description = "나의 현황", example = "% 기준")
    private Integer myCondition;

    public static ChallengeConditionResponseDto from(Challenge challenge){

        return new ChallengeConditionResponseDtoBuilder()
                .challengePeriod(challenge.getChallengePeriod())
                .ongoingDate(ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), LocalDateTime.now()))
                .averageCondition(challenge.getAverageCondition())
                .myCondition(0)
                .build();
    }

    public static ChallengeConditionResponseDto from(Challenge challenge, Member member){

        int myCondition = 0;
        for(Participate participate : challenge.getParticipates()){
            if(participate.getMember().getMemberId().equals(member.getMemberId())){
                myCondition = Math.toIntExact(Math.round(((participate.getCertifies().size() / challenge.getCertifyNum()) * 0.01)));
            }
        }

        return new ChallengeConditionResponseDtoBuilder()
                .challengePeriod(challenge.getChallengePeriod())
                .ongoingDate(ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), LocalDateTime.now()))
                .averageCondition(challenge.getAverageCondition())
                .myCondition(myCondition)
                .build();
    }



}
