package com.cmc.domains.member.service;

import com.cmc.common.exception.BadRequestException;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.domains.member.repository.nickname.NameAdjectiveRepository;
import com.cmc.domains.member.repository.nickname.NameAnimalRepository;
import com.cmc.domains.member.repository.nickname.NameNounRepository;
import com.cmc.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final NameAdjectiveRepository nameAdjectiveRepository;
    private final NameNounRepository nameNounRepository;
    private final NameAnimalRepository nameAnimalRepository;

    // 멤버 정보 업데이트
    public Member updateMember(Long memberId, String memberGender, String memberBirth, String interestChallenge) {

        Member member = findMemberOrThrow(memberId);
        member.update(memberGender, memberBirth, interestChallenge);
        return memberRepository.save(member);
    }

    // 내 정보 조회
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException("해당하는 멤버를 찾을 수 없습니다."));
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new BadRequestException("요청한 멤버는 존재하지 않습니다.");
        });
    }

    public String createName(int adjectiveIdx, int nounIdx, int animalIdx) {

        String adjective =  nameAdjectiveRepository.findById(adjectiveIdx).orElseThrow(() -> {
            throw new BadRequestException("범위 밖의 인덱스 입니다.");
        }).getAdjectiveContent();

        String noun =  nameNounRepository.findById(nounIdx).orElseThrow(() -> {
            throw new BadRequestException("범위 밖의 인덱스 입니다.");
        }).getNounContent();

        String animal =  nameAnimalRepository.findById(animalIdx).orElseThrow(() -> {
            throw new BadRequestException("범위 밖의 인덱스 입니다.");
        }).getAnimalContent();

        return adjective + noun + animal;
    }
}
