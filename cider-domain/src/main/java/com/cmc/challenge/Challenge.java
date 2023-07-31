package com.cmc.challenge;

import com.cmc.base.BaseTimeEntity;
import com.cmc.certify.Certify;
import com.cmc.challenge.constant.InterestField;
import com.cmc.challenge.constant.JudgeStatus;
import com.cmc.challenge.constant.ChallengeStatus;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.image.certifyExample.CertifyExampleImage;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private LocalDate challengeEndDate;

    private LocalDate recruitStartDate;

    private LocalDate recruitEndDate;

    private Integer recruitPeriod;

    private String certifyMission;

    private Boolean isPublic;

    private Boolean isOfficial;

    private Boolean isReward;

    private Integer certifyNum;

    private String failureRule;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_status", columnDefinition = "VARCHAR(30)")
    private ChallengeStatus challengeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "judge_status", columnDefinition = "VARCHAR(30)")
    private JudgeStatus judgeStatus;

    @Builder.Default
    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    private List<CertifyExampleImage> certifyExampleImageList = new ArrayList<>();

    public Integer getAverageCondition(){

        int cnt = 0;
        for(Participate participate : this.getParticipates()){
            cnt += participate.getCertifies().size();
        }
        return Math.toIntExact(Math.round((cnt / this.getParticipates().size()) * 0.01));
    }

    // TODO : 이 메서드 어디서 만들었던 것 같은데
    public Boolean isParticipants(Member member){

        for(Participate participate : this.getParticipates()){
            if(participate.getMember().equals(member)){
                return true;
            }
        }
        return false;
    }

    public Boolean checkCertifyToday(Member member){

        for(Participate participate : this.getParticipates()){
            if(participate.getMember().equals(member)){
                for(Certify certify : participate.getCertifies()){
                    if (certify.getCreatedDate().toLocalDate().equals(LocalDate.now())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Boolean isLike(Member member){

        for(ChallengeLike challengeLike : this.getChallengeLikes()){
            if(challengeLike.getMember().equals(member)){
                return true;
            }
        }
        return false;
    }

//    public boolean isCreator(Long memberId) {
//
//        return this.member.getMemberId().equals(memberId);
//    }

}
