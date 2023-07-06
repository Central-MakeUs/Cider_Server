package com.cmc.member;

import com.cmc.base.BaseTimeEntity;
import com.cmc.oauth.constant.SocialType;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_path", columnDefinition = "VARCHAR(200) default ''", nullable = false)
    private String profilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", columnDefinition = "VARCHAR(50)", nullable = false)
    SocialType socialType;

    @Column(name = "member_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String memberName;

    private Date memberBirth;

    private String memberGender;

    private String interestChallenge;

    private String interestKeyword;

    @Column(name = "fcm_token", columnDefinition = "VARCHAR(200)")
    private String fcmToken;

    @Column(name = "fcm_token_date", columnDefinition = "DATETIME")
    private LocalDateTime fcmTokenDate;


    public static Member create(String email, SocialType socialType) {
        return Member.builder()
                .email(email)
                .socialType(socialType)
                .memberName("") // 회원가입 할때는 빈값으로 세팅, 이후 멤버 업데이트 api 로 변경
                .profilePath("")
                .build();
    }

}
