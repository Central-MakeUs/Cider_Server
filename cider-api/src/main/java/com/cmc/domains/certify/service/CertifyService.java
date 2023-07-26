package com.cmc.domains.certify.service;

import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.NoSuchIdException;
import com.cmc.domains.certify.dto.request.CertifyCreateRequestDto;
import com.cmc.domains.certify.repository.CertifyRepository;
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
public class CertifyService {

    private final CertifyRepository certifyRepository;
    private final ParticipateRepository participateRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    // 인증 생성
    public Certify create(Long challengeId, String certifyName, String certifyContent, Long memberId) {

        // find participate
        Challenge challenge = findChallengeOrThrow(challengeId);
        Member member = findMemberOrThrow(memberId);
        Participate participate = participateRepository.findByChallengeAndMember(challenge, member);

        Certify certify = Certify.create(participate, certifyName, certifyContent);
        return certifyRepository.save(certify);
    }

    private Challenge findChallengeOrThrow(Long challengeId){
        return challengeRepository.findById(challengeId).orElseThrow(() -> {
            throw new NoSuchIdException("요청하신 챌린지는 존재하지 않습니다.");
        });
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new BadRequestException("요청한 멤버는 존재하지 않습니다.");
        });
    }
}
