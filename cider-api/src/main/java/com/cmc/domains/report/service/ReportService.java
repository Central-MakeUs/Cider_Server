package com.cmc.domains.report.service;

import com.cmc.block.Block;
import com.cmc.block.constant.BlockType;
import com.cmc.certify.Certify;
import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.NoSuchIdException;
import com.cmc.domains.certify.repository.CertifyRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.domains.report.repository.BlockRepository;
import com.cmc.domains.report.repository.ReportRepository;
import com.cmc.member.Member;
import com.cmc.report.Report;
import com.cmc.report.constant.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final CertifyRepository certifyRepository;
    private final BlockRepository blockRepository;

    // 게시글 신고하기
    public void createFeedReport(Long contentId, String reason, Long memberId) {

        Member member = findMemberOrThrow(memberId);

        Report report = Report.builder().member(findMemberOrThrow(memberId)).certify(findCertifyOrThrow(contentId))
                .reportType(ReportType.FEED).reportReason(reason).build();
        member.getReports().add(report);

        reportRepository.save(report);
    }

    // 게시글 차단하기
    public void createFeedBlock(Long contentId, String reason, Long memberId) {

        Member member = findMemberOrThrow(memberId);

        Block block = Block.builder().member(member).certify(findCertifyOrThrow(contentId))
                .blockType(BlockType.FEED).blockReason(reason).build();
        member.getBlocks().add(block);

        blockRepository.save(block);
    }

    // 사용자 신고하기
    public void createMemberReport(Long contentId, String reason, Long memberId) {

        Member member = findMemberOrThrow(memberId);

        Report report = Report.builder().member(member).certify(findCertifyOrThrow(contentId))
                .reportType(ReportType.MEMBER).reportReason(reason).build();
        member.getReports().add(report);

        reportRepository.save(report);
    }

    // 사용자 차단하기
    public void createMemberBlock(Long contentId, String reason, Long memberId) {

        Member member = findMemberOrThrow(memberId);

        Block block = Block.builder().member(member).certify(findCertifyOrThrow(contentId))
                .blockType(BlockType.MEMBER).blockReason(reason).build();
        member.getBlocks().add(block);

        blockRepository.save(block);
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new BadRequestException("요청한 멤버는 존재하지 않습니다.");
        });
    }

    private Certify findCertifyOrThrow(Long certifyId){
        return certifyRepository.findById(certifyId).orElseThrow(() -> {
            throw new NoSuchIdException("요청하신 챌린지 인증은 존재하지 않습니다.");
        });
    }

}
