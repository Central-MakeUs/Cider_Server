package com.cmc.participate;

import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import com.cmc.member.Member;
import com.cmc.participate.constant.ParticipateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participate")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participate_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long participateId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @Column(name = "participate_status", columnDefinition = "VARCHAR(30)")
    private ParticipateStatus participateStatus;

    @OneToMany(mappedBy = "participate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certify> certifies;

    private Boolean isCreator;

    public void updateIsCreator() {

        this.isCreator = true;
    }

    public static Participate create(Member member, Challenge challenge){

        return Participate.builder()
                .member(member)
                .challenge(challenge)
                .isCreator(false)
                .build();
    }
}
