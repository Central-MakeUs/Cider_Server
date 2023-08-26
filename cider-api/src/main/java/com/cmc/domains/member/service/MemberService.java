package com.cmc.domains.member.service;

import com.cmc.common.exception.BadRequestException;
import com.cmc.common.response.CommonResponse;
import com.cmc.domains.member.dto.response.LevelInfoResponseDto;
import com.cmc.domains.member.repository.MemberLevelRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.domains.member.repository.nickname.NameAdjectiveRepository;
import com.cmc.domains.member.repository.nickname.NameAnimalRepository;
import com.cmc.domains.member.repository.nickname.NameNounRepository;
import com.cmc.member.Member;
import com.cmc.member.constant.MemberType;
import com.cmc.memberLevel.MemberLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final NameAdjectiveRepository nameAdjectiveRepository;
    private final NameNounRepository nameNounRepository;
    private final NameAnimalRepository nameAnimalRepository;
    private final MemberLevelRepository memberLevelRepository;

    // 멤버 정보 업데이트
    public Member updateMember(Long memberId, String memberGender, String memberBirth, String interestChallenge, String memberName) {

        Member member = findMemberOrThrow(memberId);
        member.update(memberGender, memberBirth, interestChallenge);
        member.setIsUpdatedMember(true);
        member.updateName(memberName);
        return memberRepository.save(member);
    }

    // 내 정보 조회
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException("해당하는 멤버를 찾을 수 없습니다."));
    }

    // 프로필 업데이트
    public void updateProfile(String memberName, Long memberId) {

        Member member = findMemberOrThrow(memberId);
        member.updateName(memberName);
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

    public CommonResponse checkNickName(String nickname) {

        if(isValidNickName(nickname)){
            return CommonResponse.from("사용할 수 있는 닉네임입니다.");
        }
        else {
            return CommonResponse.from("이미 존재하는 닉네임입니다.");
        }
    }

    private boolean isValidNickName(String nickname) {

        return !memberRepository.existsByMemberName(nickname);
    }

    public LevelInfoResponseDto getNextLevel(MemberLevel memberLevel) {

        // TODO : 만렙 처리
        return memberLevelRepository.getNextLevel(memberLevel.getMemberLevelId() + 1);
    }

    public Member getAdmin() {

        return memberRepository.getAdmin(MemberType.ADMIN);
    }
}
