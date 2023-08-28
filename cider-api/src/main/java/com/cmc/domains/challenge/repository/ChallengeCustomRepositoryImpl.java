package com.cmc.domains.challenge.repository;

import com.cmc.challenge.Challenge;
import com.cmc.challenge.QChallenge;
import com.cmc.challenge.constant.ChallengeStatus;
import com.cmc.challenge.constant.InterestField;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.cmc.participate.constant.ParticipateStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import static com.cmc.challenge.QChallenge.challenge;
import static com.cmc.participate.QParticipate.participate;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChallengeCustomRepositoryImpl implements ChallengeCustomRepository{

     private final JPAQueryFactory jpaQueryFactory;

     // 홈 - 인기챌린지 조회
    @Override
    public List<ChallengeResponseVo> getPopularChallenges() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING)
                        .or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE)))
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 홈 - 공식챌린지 조회
    @Override
    public List<ChallengeResponseVo> getOfficialChallenges() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE).and(challenge.isOfficial.eq(true)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .limit(10)
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 홈 - 카테고리 조회
    @Override
    public List<ChallengeResponseVo> getCategoryChallenges(String category) {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE))
                        .and(challenge.challengeBranch.eq(InterestField.of(category))))
                .groupBy(challenge)
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 인기 챌린지 리스트 조회 - 최신순
    @Override
    public List<ChallengeResponseVo> getPopularChallengeByLatest() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 인기 챌린지 리스트 조회 - 참여순
    @Override
    public List<ChallengeResponseVo> getPopularChallengeByParticipate() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE)))
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 인기 챌린지 리스트 조회 - 좋아요순
    @Override
    public List<ChallengeResponseVo> getPopularChallengeByLike() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.challengeLikes.size().desc())
                .fetch();

        return makeChallengeResponseVos(challenges);
    }


    // 공식 챌린지 리스트 조회 - 최신순
    @Override
    public List<ChallengeResponseVo> getOfficialChallengeByLatest() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE))
                        .and(challenge.isOfficial.eq(true)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 공식 챌린지 리스트 조회 - 참여순
    @Override
    public List<ChallengeResponseVo> getOfficialChallengeByParticipate() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE))
                        .and(challenge.isOfficial.eq(true)))
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 공식 챌린지 리스트 조회 - 좋아요순
    @Override
    public List<ChallengeResponseVo> getOfficialChallengeByLike() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE))
                        .and(challenge.isOfficial.eq(true)))
                .groupBy(challenge)
                .orderBy(challenge.challengeLikes.size().desc())
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 전체 챌린지 리스트 조회 - 최신순
    @Override
    public List<ChallengeResponseVo> getChallengeByLatest() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE))
                        .or(challenge.challengeStatus.eq(ChallengeStatus.IMPOSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.createdDate.desc())
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 전체 챌린지 리스트 조회 - 참여순
    @Override
    public List<ChallengeResponseVo> getChallengeByParticipate() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE))
                        .or(challenge.challengeStatus.eq(ChallengeStatus.IMPOSSIBLE)))
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 전체 챌린지 리스트 조회 - 좋아요순
    @Override
    public List<ChallengeResponseVo> getChallengeByLike() {

        List<Challenge> challenges =  jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.challengeStatus.eq(ChallengeStatus.RECRUITING).or(challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE))
                        .or(challenge.challengeStatus.eq(ChallengeStatus.IMPOSSIBLE)))
                .groupBy(challenge)
                .orderBy(challenge.challengeLikes.size().desc())
                .fetch();

        return makeChallengeResponseVos(challenges);
    }

    // 내 챌린지 - 진행 중인 챌린지
    @Override
    public List<Challenge> getMyOngoingChallenge(Long memberId) {

        return jpaQueryFactory.selectFrom(challenge)
                .leftJoin(participate).on(challenge.challengeId.eq(participate.challenge.challengeId))
                .where(participate.participateStatus.eq(ParticipateStatus.ONGOING)
                        .and(participate.challenge.challengeStatus.eq(ChallengeStatus.POSSIBLE)
                                .or(participate.challenge.challengeStatus.eq(ChallengeStatus.IMPOSSIBLE)))
                        .and(participate.member.memberId.eq(memberId)))
                .fetch();
    }

    // 내 챌린지 - 최근 종료된 챌린지
    @Override
    public List<Challenge> getMyPassedChallenge(Long memberId) {

        return jpaQueryFactory.selectFrom(challenge)
                .leftJoin(participate).on(challenge.challengeId.eq(participate.challenge.challengeId))
                .where(challenge.challengeStatus.eq(ChallengeStatus.END)
                        .and(participate.member.memberId.eq(memberId)))
                .groupBy(challenge)
                .orderBy(challenge.challengeEndDate.desc())
                .limit(8)
                .fetch();
    }

    // 내 챌린지 - 심사중인 챌린지
    @Override
    public List<Challenge> getMyJudgingChallenge(Long memberId) {

        return jpaQueryFactory.selectFrom(challenge)
                .leftJoin(participate).on(challenge.challengeId.eq(participate.challenge.challengeId))
                .where(participate.isCreator.eq(true).and(participate.member.memberId.eq(memberId)))
                .fetch();
    }

    private List<ChallengeResponseVo> makeChallengeResponseVos(List<Challenge> challenges) {
        List<ChallengeResponseVo> responseVos = new ArrayList<>();
        for(Challenge thisChallenge : challenges){

            Integer participateNum = jpaQueryFactory.selectFrom(participate)
                    .leftJoin(challenge).on(participate.challenge.challengeId.eq(challenge.challengeId))
                    .where(participate.challenge.challengeId.eq(thisChallenge.getChallengeId()))
                    .fetch().size();

            responseVos.add(new ChallengeResponseVo(thisChallenge, participateNum));
        }
        return responseVos;
    }

    private Long getDateBetween(QChallenge challenge){

        return ChronoUnit.DAYS.between((Temporal) challenge.challengeStartDate, LocalDate.now());
    }

}
