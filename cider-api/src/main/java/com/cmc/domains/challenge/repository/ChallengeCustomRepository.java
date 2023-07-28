package com.cmc.domains.challenge.repository;

import com.cmc.challenge.Challenge;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;

import java.util.List;

public interface ChallengeCustomRepository {

    List<ChallengeResponseVo> getPopularChallenges();

    List<ChallengeResponseVo> getOfficialChallenges();

    List<ChallengeResponseVo> getCategoryChallenges(String category);

    List<ChallengeResponseVo> getPopularChallengeByLatest();

    List<ChallengeResponseVo> getPopularChallengeByParticipate();

    List<ChallengeResponseVo> getPopularChallengeByLike();

    List<ChallengeResponseVo> getOfficialChallengeByLatest();

    List<ChallengeResponseVo> getOfficialChallengeByParticipate();

    List<ChallengeResponseVo> getOfficialChallengeByLike();

    List<ChallengeResponseVo> getChallengeByLatest();

    List<ChallengeResponseVo> getChallengeByParticipate();

    List<ChallengeResponseVo> getChallengeByLike();

    List<Challenge> getMyOngoingChallenge(Long memberId);

    List<Challenge> getMyPassedChallenge(Long memberId);

    List<Challenge> getMyJudgingChallenge(Long memberId);
}
