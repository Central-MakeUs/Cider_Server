package com.cmc.challenge;

import com.cmc.base.BaseTimeEntity;
import com.cmc.challenge.constant.InterestField;
import com.cmc.challenge.constant.Status;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.image.certifyExample.CertifyExampleImage;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long challengeId;

//    @ManyToOne
//    @JoinColumn(name = "creator_id")
//    private Member member;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participate> participates;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeLike> challengeLikes;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_branch", columnDefinition = "VARCHAR(30)")
    private InterestField challengeBranch;

    private String challengeName;

    private String challengeInfo;

    private Integer challengeCapacity;

    private Integer challengePeriod;

    private LocalDate challengeStartDate;

    private Integer recruitPeriod;

    private String certifyMission;

    private Boolean isPublic;

    private Boolean isOfficial;

    private Boolean isReward;

    private Integer certifyNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_status", columnDefinition = "VARCHAR(30)")
    private Status challengeStatus;

    @Builder.Default
    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    private List<CertifyExampleImage> certifyExampleImageList = new ArrayList<>();

//    public boolean isCreator(Long memberId) {
//
//        return this.member.getMemberId().equals(memberId);
//    }

}
