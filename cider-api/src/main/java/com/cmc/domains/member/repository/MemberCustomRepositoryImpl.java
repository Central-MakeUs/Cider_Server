package com.cmc.domains.member.repository;

import com.cmc.member.Member;
import com.cmc.oauth.constant.SocialType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import static com.cmc.member.QMember.member;
import static com.cmc.memberToken.QMemberToken.memberToken;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

}
