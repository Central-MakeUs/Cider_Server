package com.cmc.domains.certify.repository;

import com.cmc.certify.Certify;

import java.util.List;

public interface CertifyCustomRepository {

    List<Certify> getCertifyList();

    List<Certify> getCertifyByChallengeLike(Long challengeId);

    List<Certify> getCertifyByChallengeRecent(Long challengeId);

    List<Certify> getChallengeCertifyList(Long challengeId);
}
