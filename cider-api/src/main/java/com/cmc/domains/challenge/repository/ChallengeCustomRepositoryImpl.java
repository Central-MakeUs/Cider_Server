package com.cmc.domains.challenge.repository;

import com.cmc.challenge.QChallenge;
import com.cmc.challenge.constant.InterestField;
import com.cmc.challenge.constant.Status;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

import static com.cmc.challenge.QChallenge.challenge;
import static com.cmc.challengeLike.QChallengeLike.challengeLike;
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

    @Override
    public List<ChallengeResponseVo> getCategoryChallenges(String category) {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE))
                        .and(challenge.challengeBranch.eq(category)))
                .groupBy(challenge)
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getPopularChallengeByLatest() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getPopularChallengeByParticipate() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(participate.count().desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getPopularChallengeByLike() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.challengeLikes.size().desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getOfficialChallengeByLatest() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.POSSIBLE)
                        .and(challenge.isOfficial.eq(true)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getOfficialChallengeByParticipate() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.POSSIBLE)
                        .and(challenge.isOfficial.eq(true)))
                .groupBy(challenge)
                .orderBy(participate.count().desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getOfficialChallengeByLike() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.POSSIBLE)
                        .and(challenge.isOfficial.eq(true)))
                .groupBy(challenge)
                .orderBy(challenge.challengeLikes.size().desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getChallengeByLatest() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getChallengeByParticipate() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(participate.count().desc())
                .fetch();
    }

    @Override
    public List<ChallengeResponseVo> getChallengeByLike() {

        return jpaQueryFactory.selectDistinct(Projections.fields(ChallengeResponseVo.class,
                        challenge,
                        participate.count()))
                .from(challenge, participate)
                .innerJoin(participate.challenge, challenge)
                .where(challenge.challengeStatus.eq(Status.RECRUITING).or(challenge.challengeStatus.eq(Status.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.challengeLikes.size().desc())
                .fetch();
    }

    private Long getDateBetween(QChallenge challenge){

        return ChronoUnit.DAYS.between((Temporal) challenge.challengeStartDate, LocalDate.now());
    }

}
