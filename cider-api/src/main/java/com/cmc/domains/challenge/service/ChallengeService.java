package com.cmc.domains.challenge.service;

import com.cmc.challenge.Challenge;
import com.cmc.challenge.constant.JudgeStatus;
import com.cmc.challenge.constant.ChallengeStatus;
import com.cmc.common.exception.NoSuchIdException;
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
        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        challenge.setJudgeStatus(JudgeStatus.JUDGING);
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

    // 공식 챌린지 리스트 조회
    public List<ChallengeResponseVo> getOfficialChallengeList(String filter) {

        List<ChallengeResponseVo> challengeResponseVos = new ArrayList<>();
        switch (filter){
            case "latest":
                challengeResponseVos = challengeRepository.getOfficialChallengeByLatest();
                break;
            case "participate":
                challengeResponseVos = challengeRepository.getOfficialChallengeByParticipate();
                break;
            case "like":
                challengeResponseVos = challengeRepository.getOfficialChallengeByLike();
                break;
        }
        return challengeResponseVos;
    }

    // 전체 챌린지 리스트 조회
    public List<ChallengeResponseVo> getChallengeList(String filter) {

        List<ChallengeResponseVo> challengeResponseVos = new ArrayList<>();
        switch (filter){
            case "latest":
                challengeResponseVos = challengeRepository.getChallengeByLatest();
                break;
            case "participate":
                challengeResponseVos = challengeRepository.getChallengeByParticipate();
                break;
            case "like":
                challengeResponseVos = challengeRepository.getChallengeByLike();
                break;
        }
        return challengeResponseVos;
    }

    // 내 챌린지 - 진행중인 챌린지
    public List<Challenge> getMyOngoingChallenge(Long memberId) {

        return challengeRepository.getMyOngoingChallenge(memberId);
    }

    // 내 챌린지 - 최근 종료된 챌린지
    public List<Challenge> getMyPassedChallenge(Long memberId) {

        return challengeRepository.getMyPassedChallenge(memberId);
    }

    // 내 챌린지 - 심사중인 챌린지
    public List<Challenge> getMyJudgingChallenge(Long memberId) {

        return challengeRepository.getMyJudgingChallenge(memberId);
    }

    // 챌린지 단일 조회
    public Challenge getChallenge(Long challengeId) {

        return challengeRepository.findById(challengeId).orElseThrow(() -> {
            throw new NoSuchIdException("요청하신 챌린지는 존재하지 않습니다.");
        });
    }
}
