package com.cmc.domains.member.repository.nickname;

import com.cmc.member.nickname.NameAdjective;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NameAdjectiveRepository extends JpaRepository<NameAdjective, Integer> {

    @Query("select n from NameAdjective n where n.adjectiveId =:adjectiveId")
    Optional<NameAdjective> findById(@Param(value = "adjectiveId") Integer adjectiveId);
}
