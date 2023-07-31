package com.cmc.domains.certify.repository;

import com.cmc.certify.Certify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.cmc.certify.QCertify.certify;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CertifyCustomRepositoryImpl implements CertifyCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Certify> getCertifyList() {

        return jpaQueryFactory
                .selectFrom(certify)
                .where(certify.createdDate.goe(LocalDateTime.now().minusDays(7)))
                .groupBy(certify)
                .orderBy(certify.certifyLikeList.size().desc())
                .limit(5)
                .fetch();
    }
}
