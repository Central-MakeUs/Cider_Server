package com.cmc.domains.participate.repository;

import com.cmc.challenge.Challenge;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, Long> {

    Participate findByChallengeAndMember(Challenge challenge, Member member);
}
