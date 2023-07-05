package com.cmc.memberToken;

import com.cmc.base.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;


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

    public static MemberToken create(String refreshToken, LocalDateTime tokenExpiredTime) {
        final MemberToken memberToken = MemberToken.builder()
                .refreshToken(refreshToken)
                .tokenExpirationTime(tokenExpiredTime)
                .build();

        return memberToken;
    }

}
