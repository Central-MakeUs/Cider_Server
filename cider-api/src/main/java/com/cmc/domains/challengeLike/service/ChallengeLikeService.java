package com.cmc.domains.challengeLike.service;

import com.cmc.challenge.Challenge;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.exception.BadRequestException;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.challengeLike.repository.ChallengeLikeRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChallengeLikeService {

    private final ChallengeLikeRepository challengeLikeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    // 관심 챌린지 생성
    public ChallengeLike create(Long memberId, Long challengeId) {

        Member member = findMemberOrThrow(memberId);
        Challenge challenge = findChallengeOrThrow(challengeId);

        ChallengeLike challengeLike = ChallengeLike.create(member, challenge);
        challenge.getChallengeLikes().add(challengeLike);

        return challengeLikeRepository.save(challengeLike);
    }

    // 관심 챌린지 삭제
    public void delete(Long memberId, Long challengeId) {

        Challenge challenge = findChallengeOrThrow(challengeId);
        List<ChallengeLike> challengeLikeList = challenge.getChallengeLikes();

        ChallengeLike challengeLike = findChallengeLike(challengeLikeList, memberId);
        if (challengeLike == null){
            throw new BadRequestException("관심 챌린지에 등록하지 않은 챌린지입니다.");
        }
        log.info("challengeLikeId ::: " + challengeLike.getChallengeLikeId());

        // 연관관계까지 삭제
        challengeLikeRepository.deleteById(challengeLike.getChallengeLikeId());
        challenge.getChallengeLikes().removeIf(like -> like.getChallengeLikeId().equals(challengeLike.getChallengeLikeId()));
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new BadRequestException("요청한 멤버는 존재하지 않습니다.");
        });
    }

    private Challenge findChallengeOrThrow(Long challengeId) {
        return challengeRepository.findById(challengeId).orElseThrow(() -> {
            throw new BadRequestException("요청한 챌린지는 존재하지 않습니다.");
        });
    }

    private ChallengeLike findChallengeLike(List<ChallengeLike> challengeLikes, Long memberId){

        ChallengeLike challengeLike = null;
        for(ChallengeLike like : challengeLikes){
            if(like.getMember().getMemberId().equals(memberId)){
                challengeLike = like;
            }
        }

        return challengeLike;
    }
}
