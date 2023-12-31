package com.cmc.member;

import com.cmc.base.BaseTimeEntity;
import com.cmc.block.Block;
import com.cmc.certifyLike.CertifyLike;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.member.constant.MemberType;
import com.cmc.memberLevel.MemberLevel;
import com.cmc.oauth.constant.SocialType;
import com.cmc.participate.Participate;
import com.cmc.report.Report;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertifyLike> certifyLikes;

    @OneToMany(mappedBy = "member")
    private List<Report> reports;

    @OneToMany(mappedBy = "member")
    private List<Block> blocks;

    @Column(name = "profile_path", columnDefinition = "VARCHAR(200) default ''")
    private String profilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", columnDefinition = "VARCHAR(50)", nullable = false)
    SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", columnDefinition = "VARCHAR(20)", nullable = false)
    MemberType memberType;

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

    private LocalDateTime isDeleted;

    private String socialId;

    public static Member create(String nickname, String email, String birthday, String gender, SocialType socialType) {

        return Member.builder()
                .memberName(nickname)
                .email(email)
                .memberBirth(birthday)
                .memberGender(gender)
                .memberLevel(new MemberLevel(1, "시작 챌린저", 0))
                .socialType(socialType)
                .memberType(MemberType.MEMBER)
                .memberExperience(100)
                .interestChallenge("") // 회원가입 할때는 빈값으로 세팅, 이후 멤버 업데이트 api 로 변경
                .isUpdatedMember(false)
                .build();
    }

    public static Member createApple(String email, SocialType socialType, String socialId) {

        return Member.builder()
                .memberName("")
                .email(email)
                .memberBirth("")
                .memberGender("")
                .memberLevel(new MemberLevel(1, "시작 챌린저", 0))
                .socialType(socialType)
                .memberType(MemberType.MEMBER)
                .memberExperience(100)
                .interestChallenge("") // 회원가입 할때는 빈값으로 세팅, 이후 멤버 업데이트 api 로 변경
                .isUpdatedMember(false)
                .socialId(socialId)
                .build();
    }

    public void update(String memberGender, String memberBirth, String interestChallenge) {

        this.memberGender = memberGender;
        this.memberBirth = memberBirth;
        this.interestChallenge = interestChallenge;
    }

    public void updateProfileImage(){

        String[] images = {
                "https://cider-bucket.s3.ap-northeast-2.amazonaws.com/profileExample/bear.png",
                "https://cider-bucket.s3.ap-northeast-2.amazonaws.com/profileExample/chick.png",
                "https://cider-bucket.s3.ap-northeast-2.amazonaws.com/profileExample/fish.png",
                "https://cider-bucket.s3.ap-northeast-2.amazonaws.com/profileExample/pig.png",
                "https://cider-bucket.s3.ap-northeast-2.amazonaws.com/profileExample/rabbit.png"
        };

        Random random = new Random();
        int selectedIndex = random.nextInt(images.length);

        this.profilePath = images[selectedIndex];
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

    public void updateIsDeleted(){

        this.isDeleted = LocalDateTime.now();
    }

    public void updateEmail(){

        this.email = this.email + "-del";
    }
}
