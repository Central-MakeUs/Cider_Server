package com.cmc.domains.member.repository.nickname;

import com.cmc.member.nickname.NameNoun;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NameNounRepository extends JpaRepository<NameNoun, Integer> {

    @Query("select n from NameNoun n where n.nounId =:nounId")
    Optional<NameNoun> findById(@Param(value = "nounId") Integer nounId);
}
