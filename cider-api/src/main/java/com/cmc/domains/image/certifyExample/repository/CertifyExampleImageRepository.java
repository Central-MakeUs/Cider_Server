package com.cmc.domains.image.certifyExample.repository;

import com.cmc.image.certifyExample.CertifyExampleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertifyExampleImageRepository extends JpaRepository<CertifyExampleImage, Long> {

    @Query("select i from CertifyExampleImage i where i.challenge.challengeId =:challengeId")
    List<CertifyExampleImage> findCertifyImage(Long challengeId);
}
