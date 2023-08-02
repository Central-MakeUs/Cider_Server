package com.cmc.domains.certify.repository;


import com.cmc.certify.Certify;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertifyRepository extends JpaRepository<Certify, Long>, CertifyCustomRepository {
}
