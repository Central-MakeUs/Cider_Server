package com.cmc.challengeLike;

import com.cmc.base.BaseTimeEntity;
import com.cmc.challenge.Challenge;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge_like")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_like_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long challengeLikeId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public static ChallengeLike create(Member member, Challenge challenge){

        return ChallengeLike.builder()
                .member(member)
                .challenge(challenge)
                .build();
    }

}
