package com.cmc.domains.challenge.repository;

import com.cmc.domains.challenge.vo.ChallengeResponseVo;

import java.util.List;

public interface ChallengeCustomRepository {

    List<ChallengeResponseVo> getPopularChallenges();

    List<ChallengeResponseVo> getOfficialChallenges();

    List<ChallengeResponseVo> getCategoryChallenges(String category);
}
