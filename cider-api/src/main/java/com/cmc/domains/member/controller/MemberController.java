package com.cmc.domains.member.controller;

import com.cmc.domains.member.dto.request.MemberUpdateReqDto;
import com.cmc.domains.member.dto.response.MemberUpdateResDto;
import com.cmc.domains.member.service.MemberService;
import com.cmc.global.resolver.RequestMemberId;
import com.cmc.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
