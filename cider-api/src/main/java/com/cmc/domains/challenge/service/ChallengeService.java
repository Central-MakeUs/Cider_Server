package com.cmc.domains.challenge.service;

import com.cmc.challenge.Challenge;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    // 카테고리 별 챌린지 조회
    public List<ChallengeResponseVo> getCategoryChallenges(String category) {

        return challengeRepository.getCategoryChallenges(category);
    }

    // 인기 챌린지 리스트 조회
    public List<ChallengeResponseVo> getPopularChallengeList(String filter) {

        List<ChallengeResponseVo> challengeResponseVos = new ArrayList<>();
        switch (filter){
            case "latest":
                challengeResponseVos = challengeRepository.getPopularChallengeByLatest();
                break;
            case "participate":
                challengeResponseVos = challengeRepository.getPopularChallengeByParticipate();
                break;
            case "like":
                challengeResponseVos = challengeRepository.getPopularChallengeByLike();
                break;
        }
        return challengeResponseVos;
    }
}
