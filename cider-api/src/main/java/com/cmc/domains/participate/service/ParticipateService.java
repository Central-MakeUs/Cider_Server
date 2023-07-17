package com.cmc.domains.participate.service;

import com.cmc.challenge.Challenge;
import com.cmc.common.exception.BadRequestException;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.domains.participate.repository.ParticipateRepository;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipateService {

    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final ParticipateRepository participateRepository;

    // 챌린지 참여하기
    public Participate create(Long challengeId, Long memberId) {

        Member member = findMemberOrThrow(memberId);
        Challenge challenge = findChallengeOrThrow(challengeId);

        Participate participate = Participate.create(member, challenge);
        participate.updateIsCreator();
        return participateRepository.save(participate);
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
