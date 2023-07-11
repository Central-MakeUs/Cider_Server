package com.cmc.domains.member.controller;

import com.cmc.domains.member.dto.request.MemberUpdateReqDto;
import com.cmc.domains.member.dto.response.MemberResponseDto;
import com.cmc.domains.member.dto.response.MemberUpdateResDto;
import com.cmc.domains.member.dto.response.RandomNameResponseDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.Random;


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

    @Tag(name = "members")
    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMe(@Parameter(hidden = true) @RequestMemberId Long memberId) {

        log.info("memberId :::: " + memberId);

        return ResponseEntity.ok(MemberResponseDto.from(memberService.find(memberId)));
    }

    @Tag(name = "members")
    @GetMapping(value = "/nickname")
    @Operation(summary = "닉네임 랜덤 생성 api")
    public ResponseEntity<RandomNameResponseDto> createName(){

        Random random = new Random();
        return ResponseEntity.ok(RandomNameResponseDto.from( memberService.createName(random.nextInt(10) + 1, random.nextInt(10) + 1, random.nextInt(10) + 1)));
    }


}
