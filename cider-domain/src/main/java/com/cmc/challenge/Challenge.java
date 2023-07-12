package com.cmc.challenge;

import com.cmc.base.BaseTimeEntity;
import com.cmc.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "challenge")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long challengeId;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Member member;

    private String challengeBranch;

    private String challengeName;

    private String challengeInfo;

    private Integer challengeCapacity;

    private Integer challengePeriod;

    private Integer recruitPeriod;

    private String certifyMission;

    private Boolean isPublic;

    private Integer certifyNum;


}
