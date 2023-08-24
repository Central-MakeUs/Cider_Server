package com.cmc.domains.report.controller;

import com.cmc.common.response.CommonResponse;
import com.cmc.domains.report.dto.request.ReportCreateRequestDto;
import com.cmc.domains.report.service.ReportService;
import com.cmc.global.resolver.RequestMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @Tag(name = "reports", description = "신고/차단하기 API")
    @Operation(summary = "게시글 신고하기 api")
    @PostMapping(value="/report/feed")
    public ResponseEntity<CommonResponse> createFeedReport(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                           @RequestBody @Valid ReportCreateRequestDto req){

        reportService.createFeedReport(req.getContentId(), req.getReason(), memberId);
        return ResponseEntity.ok(CommonResponse.from("게시글 신고가 완료되었습니다."));
    }

    @Tag(name = "reports", description = "신고/차단하기 API")
    @Operation(summary = "게시글 차단하기 api")
    @PostMapping(value="/block/feed")
    public ResponseEntity<CommonResponse> createFeedBlock(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                          @RequestBody @Valid ReportCreateRequestDto req){

        reportService.createFeedBlock(req.getContentId(), req.getReason(), memberId);
        return ResponseEntity.ok(CommonResponse.from("게시글 차단이 완료되었습니다."));
    }

    @Tag(name = "reports", description = "신고/차단하기 API")
    @Operation(summary = "사용자 신고하기 api")
    @PostMapping(value="/report/member")
    public ResponseEntity<CommonResponse> createMemberReport(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                           @RequestBody @Valid ReportCreateRequestDto req){

        reportService.createMemberReport(req.getContentId(), req.getReason(), memberId);
        return ResponseEntity.ok(CommonResponse.from("사용자 신고가 완료되었습니다."));
    }

    @Tag(name = "reports", description = "신고/차단하기 API")
    @Operation(summary = "사용자 차단하기 api")
    @PostMapping(value="/block/member")
    public ResponseEntity<CommonResponse> createMemberBlock(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                          @RequestBody @Valid ReportCreateRequestDto req){

        reportService.createMemberBlock(req.getContentId(), req.getReason(), memberId);
        return ResponseEntity.ok(CommonResponse.from("사용자 차단이 완료되었습니다."));
    }

}
