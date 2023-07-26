package com.cmc.domains.certify.controller;

import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import com.cmc.domains.certify.dto.request.CertifyCreateRequestDto;
import com.cmc.domains.certify.dto.response.CertifyCreateResponseDto;
import com.cmc.domains.certify.service.CertifyService;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.dto.response.ChallengeCreateResponseDto;
import com.cmc.global.resolver.RequestMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "certify", description = "챌린지 인증 API")
@RequestMapping("/api/certify")
public class CertifyController {

    private final CertifyService certifyService;

    @Tag(name = "certify", description = "챌린지 인증 API")
    @Operation(summary = "챌린지 인증 api")
    @PostMapping(value="")
    public ResponseEntity<CertifyCreateResponseDto> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                      @RequestBody @Valid CertifyCreateRequestDto req){

        Certify certify = certifyService.create(req.getChallengeId(), req.getCertifyName(), req.getCertifyContent(), memberId);
        return ResponseEntity.ok(CertifyCreateResponseDto.create(certify));
    }

}
