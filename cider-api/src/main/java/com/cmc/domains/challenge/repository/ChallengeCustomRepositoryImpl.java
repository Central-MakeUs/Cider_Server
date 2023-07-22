package com.cmc.domains.challenge.repository;

import com.cmc.challenge.constant.Status;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.challenge.QChallenge.challenge;
import static com.cmc.participate.QParticipate.participate;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChallengeCustomRepositoryImpl implements ChallengeCustomRepository{

     private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChallengeResponseVo> getPopularChallenges() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                challenge,
                participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(participate.count().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getOfficialChallenges() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.POSSIBLE).and(challenge.isOfficial.eq(true)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .limit(10)
                .fetch();
    }

}
