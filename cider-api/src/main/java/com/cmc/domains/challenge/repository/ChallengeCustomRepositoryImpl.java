package com.cmc.domains.challenge.repository;

import com.cmc.challenge.Challenge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChallengeCustomRepositoryImpl implements ChallengeCustomRepository{

     //private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Challenge> getPopularChallenges() {
       // return jpaQueryFactory.select(challenge)
        return null;
    }
}
