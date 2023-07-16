package com.cmc.memberToken;

import com.cmc.base.BaseTimeEntity;
import com.cmc.memberToken.constant.RemainingTokenTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Entity
@Table(name = "member_token")
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberToken extends BaseTimeEntity {

    @Id
    @Column(name = "member_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberTokenId;

    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    private Long memberId;

    public static MemberToken create(String refreshToken, LocalDateTime tokenExpiredTime, Long memberId) {
        final MemberToken memberToken = MemberToken.builder()
                .refreshToken(refreshToken)
                .tokenExpirationTime(tokenExpiredTime)
                .memberId(memberId)
                .build();

        return memberToken;
    }

    public void updateRefreshTokenExpireTime(LocalDateTime now, RemainingTokenTime remainingTokenTime) {
        final long hours = ChronoUnit.HOURS.between(now, tokenExpirationTime);
        if (hours <= remainingTokenTime.getRemainingTime()) {
            updateTokenExpireTime(now.plusWeeks(2));
        }
    }

    public void updateTokenExpireTime(LocalDateTime tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public void expire(LocalDateTime now) {
        if (tokenExpirationTime.isAfter(now)) {
            this.tokenExpirationTime = now;
        }
    }

}
