package com.cmc.domains.certify.repository;


import com.cmc.certify.Certify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CertifyRepository extends JpaRepository<Certify, Long>, CertifyCustomRepository {

    @Query("select c from Certify c where c.participate.member.memberId =:memberId")
    List<Certify> getMyCertifyList(@Param(value = "memberId") Long memberId);
}
