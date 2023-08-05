package com.cmc.domains.member.repository;

import com.cmc.domains.member.dto.response.LevelInfoResponseDto;
import com.cmc.memberLevel.MemberLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLevelRepository extends JpaRepository<MemberLevel, Integer> {

    @Query("select l from MemberLevel l where l.memberLevelId =:i+1")
    LevelInfoResponseDto getNextLevel(int i);
}
