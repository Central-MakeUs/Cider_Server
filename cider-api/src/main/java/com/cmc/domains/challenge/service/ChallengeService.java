package com.cmc.domains.challenge.service;

import com.cmc.challenge.Challenge;
import com.cmc.challenge.constant.JudgeStatus;
import com.cmc.challenge.constant.ChallengeStatus;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.CiderException;
import com.cmc.common.exception.NoSuchIdException;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    // 챌린지 생성
    public Challenge create(ChallengeCreateRequestDto req, Long memberId) {

        Challenge challenge = req.toEntity();

        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        challenge.setJudgeStatus(JudgeStatus.JUDGING);
        challenge.setIsOfficial(false);
        challenge.setCertifyNum(req.getChallengePeriod() * 7);
        challenge.setIsReward(false);
        challenge.setFailureRule(getFailureRule(req.getChallengePeriod()));

        return challengeRepository.save(challenge);
    }

    private int getFailureRule(Integer challengePeriod){

        int failureRule = 0;
        switch(challengePeriod){
            case 1 :
                failureRule = 4; break;
            case 2 :
                failureRule = 7; break;
            case 3 :
            case 4 :
                failureRule = 14; break;
            case 5 :
            case 6 :
                failureRule = 21; break;
            case 7 :
            case 8 :
                failureRule = 28; break;
        }
        return failureRule;
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
                List<ChallengeResponseVo> sortedList = sortChallengeResponseList(challengeResponseVos); // 정렬
                break;
            case "like":
                challengeResponseVos = challengeRepository.getChallengeByLike();
                break;
        }
        return challengeResponseVos;
    }

     private static List<ChallengeResponseVo> sortChallengeResponseList(List<ChallengeResponseVo> list) {
        Collections.sort(list, new Comparator<ChallengeResponseVo>() {
            @Override
            public int compare(ChallengeResponseVo cr1, ChallengeResponseVo cr2) {
                return cr2.getParticipateNum().compareTo(cr1.getParticipateNum());
            }
        });
        return list;
    }

    // 내 챌린지 - 진행중인 챌린지
    public List<Challenge> getMyOngoingChallenge(Long memberId) {

        List<Challenge> challenges = challengeRepository.getMyOngoingChallenge(memberId);
        updateExperience(challenges, memberId);
        return challenges;
    }

    private void updateExperience(List<Challenge> challenges, long memberId){

        for(Challenge challenge : challenges){
            for(Participate participate : challenge.getParticipates()){
                if(participate.getMember().getMemberId().equals(memberId)){
                    if(participate.getIsFirstCertify()){

                        // 챌린지 첫 입장 경험치 +100
                        participate.getMember().setMemberExperience(participate.getMember().getMemberExperience() + 100);
                    }
                }
            }
        }

    }

    // 내가 참여한 챌린지 조회
    // TODO : 기존 메서드랑 합쳐보기
    public List<Participate> getMyParticipateChallenge(Long memberId) {

        Member member = findMemberOrThrow(memberId);
        return member.getParticipates();
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

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> {
            throw new NoSuchIdException("요청하신 챌린지는 존재하지 않습니다.");
        });

        if(challenge.getJudgeStatus().equals(JudgeStatus.JUDGING)){
            throw new CiderException("심사중인 챌린지 입니다.");
        }
        return challenge;
    }

    // 마이페이지 - 관심 챌린지 조회
    public List<Challenge> getMyChallengeLike(Long memberId) {

        Member member = findMemberOrThrow(memberId);
        return member.getChallengeLikes().stream()
                .map(ChallengeLike::getChallenge)
                .collect(Collectors.toList());
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new BadRequestException("요청한 멤버는 존재하지 않습니다.");
        });

    }
}
