package com.cmc.domains.member.controller;

import com.cmc.common.response.CommonResponse;
import com.cmc.domains.image.service.ImageService;
import com.cmc.domains.member.dto.request.MemberProfileUpdateRequestDto;
import com.cmc.domains.member.dto.request.MemberUpdateReqDto;
import com.cmc.domains.member.dto.response.*;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "members", description = "멤버 API")
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final ImageService imageService;

    @Tag(name = "members", description = "멤버 API")
    @Operation(summary = "멤버 업데이트")
    @PatchMapping(value = "")
    public ResponseEntity<MemberUpdateResDto> updateMember(@Parameter(hidden = true) @RequestMemberId Long memberId, @Valid @RequestBody MemberUpdateReqDto request) {

        Member member = memberService.updateMember(memberId, request.getMemberGender(), request.getMemberBirth(), request.getInterestChallenge());
        return ResponseEntity.ok(MemberUpdateResDto.from(member));
    }

    @Tag(name = "members", description = "멤버 API")
    @Operation(summary = "내 정보 조회 api")
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMe(@Parameter(hidden = true) @RequestMemberId Long memberId) {

        return ResponseEntity.ok(MemberResponseDto.from(memberService.find(memberId)));
    }

    @Tag(name = "myPage", description = "마이페이지 API")
    @Operation(summary = "프로필 수정 api")
    @PatchMapping(value = "/profile")
    public ResponseEntity<CommonResponse> updateProfileImage(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                             @Valid @RequestBody MemberProfileUpdateRequestDto request) {

        memberService.updateProfile(request.getMemberName(), request.getMemberIntro(), memberId);
        return ResponseEntity.ok(CommonResponse.from("프로필 수정이 완료되었습니다."));
    }

    @Tag(name = "myPage", description = "마이페이지 API")
    @Operation(summary = "프로필 이미지 수정 api")
    @PatchMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> updateProfileImage(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                 @RequestPart(value = "profileImage") MultipartFile profileImage) throws IOException {

        imageService.updateProfileImage(profileImage, memberId);
        return ResponseEntity.ok(CommonResponse.from("프로필 이미지 수정이 완료되었습니다."));
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
        LevelInfoResponseDto nextLevel = memberService.getNextLevel(member.getMemberLevel());
        MyLevelInfoResponseDto myLevelInfo = MyLevelInfoResponseDto.from(member, LevelInfoResponseDto.from(member.getMemberLevel()), nextLevel);

        return ResponseEntity.ok(MyPageResponseDto.from(simpleMember, myActivityInfo, myLevelInfo));
    }

}
