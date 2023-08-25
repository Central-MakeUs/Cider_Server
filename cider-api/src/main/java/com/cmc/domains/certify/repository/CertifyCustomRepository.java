package com.cmc.domains.certify.repository;

import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import com.cmc.member.Member;

import java.util.List;

public interface CertifyCustomRepository {

    List<Certify> getCertifyList();

    List<Certify> getCertifyByChallengeLike(Long challengeId);

    List<Certify> getCertifyByChallengeRecent(Long challengeId);

    List<Certify> getChallengeCertifyList(Member member, Challenge challenge);
}
