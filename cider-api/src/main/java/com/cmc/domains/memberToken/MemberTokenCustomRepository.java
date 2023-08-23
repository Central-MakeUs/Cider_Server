package com.cmc.domains.memberToken;

import java.time.LocalDateTime;

public interface MemberTokenCustomRepository {

    void updateExpirationTimeByMemberId(Long memberId, LocalDateTime now);
}
