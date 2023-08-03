package com.cmc.domains.member.controller;

import com.cmc.common.response.CommonResponse;
import com.cmc.domains.member.dto.request.MemberUpdateReqDto;
import com.cmc.domains.member.dto.response.MemberResponseDto;
import com.cmc.domains.member.dto.response.MemberUpdateResDto;
import com.cmc.domains.member.dto.response.RandomNameResponseDto;
import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import com.cmc.domains.member.dto.response.mypage.MyActivityInfoResponseDto;
import com.cmc.domains.member.dto.response.mypage.MyLevelInfoResponseDto;
import com.cmc.domains.member.dto.response.mypage.MyPageResponseDto;
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

    @Tag(name = "members", description = "멤버 API")
    @Operation(summary = "멤버 업데이트")
    @PatchMapping(value = "")
    public ResponseEntity<MemberUpdateResDto> updateTeam(@Parameter(hidden = true) @RequestMemberId Long memberId, @Valid @RequestBody MemberUpdateReqDto request) {

        Member member = memberService.updateMember(memberId, request.getMemberGender(), request.getMemberBirth(), request.getInterestChallenge());
        return ResponseEntity.ok(MemberUpdateResDto.from(member));
    }

    @Tag(name = "members", description = "멤버 API")
    @Operation(summary = "내 정보 조회 api")
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMe(@Parameter(hidden = true) @RequestMemberId Long memberId) {

        return ResponseEntity.ok(MemberResponseDto.from(memberService.find(memberId)));
    }

    @Tag(name = "members", description = "멤버 API")
    @GetMapping(value = "/nicknames")
    @Operation(summary = "닉네임 랜덤 생성 api")
    public ResponseEntity<RandomNameResponseDto> createName(){

        Random random = new Random();
        return ResponseEntity.ok(RandomNameResponseDto.from( memberService.createName(random.nextInt(10) + 1, random.nextInt(10) + 1, random.nextInt(10) + 1)));
    }


    @Tag(name = "members", description = "멤버 API")
    @GetMapping("/nicknames/exists/{nickname}")
    @Operation(summary = "닉네임 중복 확인 api", description = "- message : 사용할 수 있는 닉네임입니다. / 이미 존재하는 닉네임입니다.")
    public ResponseEntity<CommonResponse> checkNickname(@PathVariable("nickname") String nickname) {

        return ResponseEntity.ok(memberService.checkNickName(nickname));
    }

    @Tag(name = "myPage", description = "마이페이지 API")
    @Operation(summary = "마이페이지 홈 조회 api")
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPage(@Parameter(hidden = true) @RequestMemberId Long memberId) {

        Member member = memberService.find(memberId);

        // 간단 내정보
        SimpleMemberResponseDto simpleMember = SimpleMemberResponseDto.from(member);

        // 활동 정보
        MyActivityInfoResponseDto myActivityInfo = MyActivityInfoResponseDto.from(member);

        // TODO : 레벨, 경험치 정보 - 로직 구체화
        MyLevelInfoResponseDto myLevelInfo = null;

        return ResponseEntity.ok(MyPageResponseDto.from(simpleMember, myActivityInfo, myLevelInfo));
    }

}
