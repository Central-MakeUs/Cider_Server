package com.cmc.member;

import com.cmc.base.BaseTimeEntity;
import com.cmc.challenge.Challenge;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.memberLevel.MemberLevel;
import com.cmc.oauth.constant.SocialType;
import com.cmc.participate.Participate;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", unique = true)
    private String email;

//    @OneToMany(mappedBy = "member")
//    private List<Challenge> challenges;

    @OneToMany(mappedBy = "member")
    private List<Participate> participates;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeLike> challengeLikes;

    @Column(name = "profile_path", columnDefinition = "VARCHAR(200) default ''")
    private String profilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", columnDefinition = "VARCHAR(50)", nullable = false)
    SocialType socialType;

    @Column(name = "member_name", columnDefinition = "VARCHAR(50)")
    private String memberName;

    private String memberIntro;

    private String memberBirth;     // TODO : memberBirth 타입 점검

    private String memberGender;

    private String interestChallenge;

    @OneToOne
    @JoinColumn(name = "member_level_id")
    private MemberLevel memberLevel; // 레벨

    private Integer memberExperience;   // 경험치

    private Boolean isUpdatedMember; // 멤버 업데이트 api 실행여부

    @Column(name = "fcm_token", columnDefinition = "VARCHAR(200)")
    private String fcmToken;

    @Column(name = "fcm_token_date", columnDefinition = "DATETIME")
    private LocalDateTime fcmTokenDate;

    public static Member create(String nickname, String email, String birthday, String gender, SocialType socialType) {

        return Member.builder()
                .memberName(nickname)
                .email(email)
                .memberBirth(birthday)
                .memberGender(gender)
                .memberLevel(new MemberLevel(1, "시작 챌린저", 0))
                .socialType(socialType)
                .interestChallenge("") // 회원가입 할때는 빈값으로 세팅, 이후 멤버 업데이트 api 로 변경
                .isUpdatedMember(false)
                .build();
    }

    public static Member createApple(String email, SocialType socialType) {

        return Member.builder()
                .memberName("")
                .email("")
                .memberBirth("")
                .memberGender("")
                .memberLevel(new MemberLevel(1, "시작 챌린저", 0))
                .socialType(socialType)
                .interestChallenge("") // 회원가입 할때는 빈값으로 세팅, 이후 멤버 업데이트 api 로 변경
                .isUpdatedMember(false)
                .build();
    }

    public void update(String memberGender, String memberBirth, String interestChallenge) {

        this.memberGender = memberGender;
        this.memberBirth = memberBirth;
        this.interestChallenge = interestChallenge;
    }

    public void updateProfileImage(String imageUrl){

        this.profilePath = imageUrl;
    }

    public void updateName(String memberName){

        this.memberName = memberName;
    }

    public void updateIntro(String memberIntro){

        this.memberIntro = memberIntro;
    }
}
