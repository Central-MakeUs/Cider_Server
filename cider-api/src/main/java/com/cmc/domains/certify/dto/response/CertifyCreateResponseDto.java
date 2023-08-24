package com.cmc.domains.certify.dto.response;

import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Random;

@Data
@Builder
@AllArgsConstructor
@Slf4j
public class CertifyCreateResponseDto {

    @Schema(description = "챌린지 인증 id", example = "10")
    private Long certifyId;

    @Schema(description = "챌린지 인증 문구", example = "오늘도 멋지게 해내셨네요!")
    private String certifyComment;

    @Schema(description = "획득 경험치", example = "350")
    private Integer certifyExperience;

    @Schema(description = "~% 진행중", example = "45")
    private Integer ongoingPercent;

    public static CertifyCreateResponseDto create(Certify certify) {

        Challenge challenge = certify.getParticipate().getChallenge();

        return CertifyCreateResponseDto.builder()
                .certifyId(certify.getCertifyId())
                .certifyComment(getRandomComment())
                .certifyExperience(10)
                .ongoingPercent((int)(((double)ChronoUnit.DAYS.between((Temporal) challenge.getChallengeStartDate(), LocalDate.now())) / (double)challenge.getChallengePeriod() * 100))
                .build();
    }

    private static String getRandomComment(){

        String[] comments = {
                "하루하루 발전하는 당신을 응원합니다!",
                "지금의 작은 노력이 미래의 큰 성취로 이어질 거예요",
                "당신의 열정과 노력에 박수를",
                "오늘도 멋지게 해내셨네요!",
                "지금이 당신의 도전을 응원합니다!"
        };

        Random random = new Random();
        int selectedIndex = random.nextInt(comments.length);
        return comments[selectedIndex];
    }
}

