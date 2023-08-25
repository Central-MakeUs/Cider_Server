package com.cmc.domains.certify.service;

import com.cmc.certify.Certify;
import com.cmc.certifyLike.CertifyLike;
import com.cmc.challenge.Challenge;
import com.cmc.challenge.constant.InterestField;
import com.cmc.challenge.constant.JudgeStatus;
import com.cmc.challengeLike.ChallengeLike;

import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.CiderException;
import com.cmc.common.exception.NoSuchIdException;
import com.cmc.domains.certify.repository.CertifyRepository;
import com.cmc.domains.certifyLike.repository.CertifyLikeRepository;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.domains.participate.repository.ParticipateRepository;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CertifyService {

    private final CertifyRepository certifyRepository;
    private final ParticipateRepository participateRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final CertifyLikeRepository certifyLikeRepository;

    // 인증 생성
    public Certify create(Long challengeId, String certifyName, String certifyContent, Long memberId) {

        // find participate
        Challenge challenge = findChallengeOrThrow(challengeId);
        Member member = findMemberOrThrow(memberId);
        Participate participate = participateRepository.findByChallengeAndMember(challenge, member);

        Certify certify = Certify.create(participate, certifyName, certifyContent);
        member.setMemberExperience(member.getMemberExperience() + 10);  // 챌린지 인증 - 경험치 +10
        return certifyRepository.save(certify);
    }

    // 홈 추천 피드 조회
    public List<Certify> getCertifyList() {

        return certifyRepository.getCertifyList();
    }

    // 챌린지 - 인증 리스트 조회
    public List<Certify> getChallengeCertifyList(Challenge challenge, String filter) {

        if(filter.equals("like")){
            return certifyRepository.getCertifyByChallengeLike(challenge.getChallengeId());
        }else {
            return certifyRepository.getCertifyByChallengeRecent(challenge.getChallengeId());
        }


    }

    // 인증 좋아요
    public CertifyLike createLike(Long memberId, Long certifyId) {

        Member member = findMemberOrThrow(memberId);
        Certify certify = findCertifyOrThrow(certifyId);

        CertifyLike certifyLike = CertifyLike.create(member, certify);
        certify.getCertifyLikeList().add(certifyLike);

        return certifyLikeRepository.save(certifyLike);
    }

    // 인증 좋아요 삭제
    public void deleteLike(Long memberId, Long certifyId) {

        Certify certify = findCertifyOrThrow(certifyId);
        List<CertifyLike> certifyLikeList = certify.getCertifyLikeList();

        CertifyLike certifyLike = findCertifyLike(certifyLikeList, memberId);
        if (certifyLike == null){
            throw new BadRequestException("좋아요가 등록하지 않은 인증글입니다.");
        }

        certifyLikeRepository.deleteById(certifyLike.getCertifyLikeId());
        certify.getCertifyLikeList().removeIf(like -> like.getCertifyLikeId().equals(certifyLike.getCertifyLikeId()));

    }

    private CertifyLike findCertifyLike(List<CertifyLike> certifyLikes, Long memberId){

        CertifyLike certifyLike = null;
        for(CertifyLike like : certifyLikes){
            if(like.getMember().getMemberId().equals(memberId)){
                certifyLike = like;
            }
        }

        return certifyLike;
    }

    public List<Certify> getMyCertifyList(Member member, Challenge challenge) {

        return certifyRepository.getChallengeCertifyList(member, challenge);
    }

    // 챌린지 삭제
    public void deleteChallenge(Long memberId, Long challengeId) {

        Challenge challenge = findChallengeOrThrow(challengeId);
        Member member = findMemberOrThrow(memberId);

        if(challenge.getJudgeStatus().equals(JudgeStatus.COMPLETE)){
            throw new CiderException("심사완료된 챌린지는 삭제할 수 없습니다.");
        }

        for(Participate participate : member.getParticipates()){
            if(participate.getChallenge().equals(challenge)){
                if(!participate.getIsCreator()){
                    throw new CiderException("챌린지를 삭제할 권한이 없습니다.");
                }
                else{
                    challengeRepository.deleteById(challengeId);
                }
            }
        }
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

    private Certify findCertifyOrThrow(Long certifyId) {
        return certifyRepository.findById(certifyId).orElseThrow(() -> {
            throw new BadRequestException("요청한 인증은 존재하지 않습니다.");
        });
    }
}
