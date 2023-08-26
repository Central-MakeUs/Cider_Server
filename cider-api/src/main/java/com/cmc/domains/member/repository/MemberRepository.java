package com.cmc.domains.member.repository;


import com.cmc.domains.member.dto.response.LevelInfoResponseDto;
import com.cmc.member.Member;
import com.cmc.member.constant.MemberType;
import com.cmc.oauth.constant.SocialType;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository{

    @Query("select m from Member m where m.email =:email")
    Optional<Member> findByEmail(@Param(value = "email") String email);

    boolean existsByMemberName(String nickName);

    @Query("select m from Member m where m.memberType =:memberType")
    Member getAdmin(MemberType memberType);

    @Query("select m from Member m where m.socialType =:socialType and m.socialId =:socialId")
    Optional<Member> findByMemberBySocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query("select m from Member m where m.email =:email and m.socialType =:socialType")
    Optional<Member> findByEmailAndSocialType(String email, SocialType socialType);
}
