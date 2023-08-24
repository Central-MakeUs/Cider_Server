package com.cmc.block;

import com.cmc.base.BaseTimeEntity;
import com.cmc.block.constant.BlockType;
import com.cmc.certify.Certify;
import com.cmc.member.Member;
import com.cmc.participate.Participate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "block")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Block extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long blockId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "certify_id")
    private Certify certify;

    @ManyToOne
    @JoinColumn(name = "blocker_id")
    private Member blocker;

    @Enumerated(EnumType.STRING)
    private BlockType blockType;

    private String blockReason;

}
