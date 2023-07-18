package com.cmc.domains.challengeLike.service;

import com.cmc.challenge.Challenge;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.exception.BadRequestException;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.challengeLike.repository.ChallengeLikeRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeLikeService {

    private final ChallengeLikeRepository challengeLikeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    // 관심 챌린지 생성
    public ChallengeLike create(Long memberId, Long challengeId) {

        Member member = findMemberOrThrow(memberId);
        Challenge challenge = findChallengeOrThrow(challengeId);

        ChallengeLike challengeLike = ChallengeLike.create(member, challenge);
        return challengeLikeRepository.save(challengeLike);
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
}
