package com.cmc.domains.member.controller;

import com.cmc.domains.member.dto.request.MemberUpdateReqDto;
import com.cmc.domains.member.dto.response.MemberUpdateResDto;
import com.cmc.domains.member.service.MemberService;
import com.cmc.global.resolver.RequestMemberId;
import com.cmc.member.Member;
import com.cmc.oauth.dto.request.OauthReqDto;
import com.cmc.oauth.dto.response.ResponseJwtTokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "members", description = "멤버 API")
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Tag(name = "members")
    @Operation(summary = "멤버 업데이트")
    @PatchMapping(value = "")
    public ResponseEntity<MemberUpdateResDto> updateTeam(@Parameter(hidden = true) @RequestMemberId Long memberId, @Valid @RequestBody MemberUpdateReqDto request) {

        Member member = memberService.updateMember(memberId, request.getMemberGender(), request.getMemberBirth(), request.getInterestChallenge());
        return ResponseEntity.ok(MemberUpdateResDto.from(member));
    }

//    @Tag(name = "members")
//    @GetMapping(value = "/nickname")
//    @Operation(summary = "닉네임 랜덤 생성 api")
//    public ResponseEntity<ResponseJwtTokenDto> createName(@Valid @RequestBody OauthReqDto oauthReqDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
//
//        return ResponseEntity.ok(jwtTokenDto);
//    }


}
