package com.cmc.domains.memberToken;

import com.cmc.memberToken.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

    Optional<MemberToken> findByRefreshToken(String refreshToken);
}
