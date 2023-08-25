package com.cmc.domains.certify.repository;

import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import com.cmc.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.cmc.certify.QCertify.certify;
import static com.cmc.challenge.QChallenge.challenge;
import static com.cmc.participate.QParticipate.participate;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CertifyCustomRepositoryImpl implements CertifyCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Certify> getCertifyList() {

        return jpaQueryFactory
                .selectFrom(certify)
                .where(certify.createdDate.goe(LocalDateTime.now().minusDays(7)))
                .groupBy(certify)
                .orderBy(certify.certifyLikeList.size().desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<Certify> getCertifyByChallengeLike(Long challengeId) {

        return jpaQueryFactory
                .selectFrom(certify)
                .leftJoin(participate).on(participate.participateId.eq(certify.participate.participateId))
                .leftJoin(certify).on(certify.participate.participateId.eq(participate.participateId))
                .where(certify.participate.challenge.challengeId.eq(challengeId))
                .groupBy(certify)
                .orderBy(certify.certifyLikeList.size().desc())
                .fetch();
    }

    @Override
    public List<Certify> getCertifyByChallengeRecent(Long challengeId) {

        return jpaQueryFactory
                .selectFrom(certify)
                .leftJoin(participate).on(participate.participateId.eq(certify.participate.participateId))
                .leftJoin(certify).on(certify.participate.participateId.eq(participate.participateId))
                .where(certify.participate.challenge.challengeId.eq(challengeId))
                .groupBy(certify)
                .orderBy(certify.createdDate.desc())
                .fetch();
    }

    @Override
    public List<Certify> getChallengeCertifyList(Member member, Challenge challenge) {

        return jpaQueryFactory
                .selectFrom(certify)
//                .leftJoin(participate).on(participate.participateId.eq(certify.participate.participateId))
//                .leftJoin(challenge).on(challenge.challengeId.eq(participate.challenge.challengeId))
                .where(certify.participate.challenge.eq(challenge)
                        .and(certify.participate.member.eq(member)))
                .fetch();
    }
}
