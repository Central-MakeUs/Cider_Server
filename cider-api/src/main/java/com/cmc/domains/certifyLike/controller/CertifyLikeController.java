package com.cmc.domains.certifyLike.controller;

import com.cmc.common.response.CommonResponse;
import com.cmc.domains.certify.dto.request.CertifyLikeCreateRequestDto;
import com.cmc.domains.certify.service.CertifyService;
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
@RequestMapping("/api/certify/like")
public class CertifyLikeController {

    private final CertifyService certifyService;

    @Tag(name = "certifyLike", description = "인증 좋아요 API")
    @Operation(summary = "인증 좋아요 등록 api")
    @PostMapping(value="/like")
    public ResponseEntity<CommonResponse> createCertifyLike(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                            @RequestBody @Valid CertifyLikeCreateRequestDto req){

        certifyService.createLike(memberId, req.getCertifyId());
        return ResponseEntity.ok(CommonResponse.from("인증글 좋아요가 등록되었습니다"));
    }

    @Tag(name = "certifyLike", description = "인증 좋아요 API")
    @Operation(summary = "인증 좋아요 삭제 api")
    @DeleteMapping(value="/like/{certifyId}")
    public ResponseEntity<CommonResponse> deleteCertifyLike(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                            @PathVariable("certifyId") Long certifyId) {

        certifyService.deleteLike(memberId, certifyId);
        return ResponseEntity.ok(CommonResponse.from("인증글 좋아요가 삭제되었습니다"));
    }

}
