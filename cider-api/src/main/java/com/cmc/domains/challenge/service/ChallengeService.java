package com.cmc.domains.challenge.service;

import com.cmc.challenge.Challenge;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    // 챌린지 생성
    public Challenge create(ChallengeCreateRequestDto req, Long memberId) {

        Challenge challenge = req.toEntity();
        return challengeRepository.save(challenge);
    }

    // 인기 챌린지 조회
    public List<ChallengeResponseVo> getPopularChallenges() {

        return challengeRepository.getPopularChallenges();
    }

    // 공식 챌린지 조회
    public List<ChallengeResponseVo> getOfficialChallenges() {

        return challengeRepository.getOfficialChallenges();
    }
}
