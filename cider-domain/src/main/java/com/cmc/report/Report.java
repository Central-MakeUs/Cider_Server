package com.cmc.report;

import com.cmc.base.BaseTimeEntity;
import com.cmc.certify.Certify;
import com.cmc.challenge.constant.InterestField;
import com.cmc.member.Member;
import com.cmc.report.constant.ReportType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "certify_id")
    private Certify certify;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    private String reportReason;
}
