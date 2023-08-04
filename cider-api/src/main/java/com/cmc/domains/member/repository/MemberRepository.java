package com.cmc.domains.member.repository;


import com.cmc.domains.member.dto.response.LevelInfoResponseDto;
import com.cmc.member.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.email =:email")
    Optional<Member> findByEmail(@Param(value = "email") String email);

    boolean existsByMemberName(String nickName);


    LevelInfoResponseDto getNextLevel(Integer memberLevelId);
}
