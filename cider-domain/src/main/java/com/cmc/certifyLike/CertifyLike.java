package com.cmc.certifyLike;

import com.cmc.certify.Certify;
import com.cmc.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certify_like")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertifyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certify_like_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long certifyLikeId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "certify_id")
    private Certify certify;

    public static CertifyLike create(Member member, Certify certify){

        return CertifyLike.builder()
                .member(member)
                .certify(certify)
                .build();
    }
}
