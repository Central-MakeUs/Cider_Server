package com.cmc.domains.challenge.repository;

import com.cmc.challenge.Challenge;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeCustomRepository {

}
