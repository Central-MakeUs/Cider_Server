package com.cmc.domains.challenge.controller;

import com.cmc.block.Block;
import com.cmc.block.constant.BlockType;
import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import com.cmc.challenge.constant.ChallengeStatus;
import com.cmc.challenge.constant.JudgeStatus;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.response.CommonResponse;
import com.cmc.domains.certify.dto.response.CertifyResponseDto;
import com.cmc.domains.certify.dto.response.SimpleCertifyResponseDto;
import com.cmc.domains.certify.service.CertifyService;
import com.cmc.domains.challenge.dto.request.ChallengeCreateRequestDto;
import com.cmc.domains.challenge.dto.request.ChallengeParticipateRequestDto;
import com.cmc.domains.challenge.dto.response.*;
import com.cmc.domains.challenge.dto.response.detail.*;
import com.cmc.domains.challenge.dto.response.my.*;
import com.cmc.domains.challenge.service.ChallengeService;
import com.cmc.domains.challenge.vo.ChallengeResponseVo;
import com.cmc.domains.image.service.ImageService;
import com.cmc.domains.member.dto.response.SimpleMemberResponseDto;
import com.cmc.domains.member.service.MemberService;
import com.cmc.domains.participate.service.ParticipateService;
import com.cmc.global.resolver.RequestMemberId;
import com.cmc.image.certifyExample.CertifyExampleImage;
import com.cmc.member.Member;
import com.cmc.oauth.service.TokenProvider;
import com.cmc.participate.Participate;
import com.cmc.participate.constant.ParticipateStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ImageService imageService;
    private final MemberService memberService;
    private final ParticipateService participateService;
    private final CertifyService certifyService;

    @Tag(name = "challenge", description = "챌린지 API")
    @Operation(summary = "챌린지 생성 api")
    @PostMapping(value="")
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                    @RequestBody @Valid ChallengeCreateRequestDto req){

        Challenge challenge = challengeService.create(req, memberId);
        participateService.createFirst(challenge.getChallengeId(), memberId);
        return ResponseEntity.ok(ChallengeCreateResponseDto.create(challenge));
    }

    @Tag(name = "challenge", description = "챌린지 API")
    @Operation(summary = "챌린지 인증 예시 이미지 업로드 api", description = "- form-data 형태로 보내주시고, content-type는 따로 지정 안해주셔도 됩니다.\n" +
            "- challengeId는 직전에 호출한 챌린지 생성 api response값 보내주시면 됩니다.")
    @PostMapping(value="/images/{challengeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> createSuccessExampleImages(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                     @RequestPart(value = "successExampleImages") List<MultipartFile> successExampleImages,
                                                                     @RequestPart(value = "failureExampleImages") List<MultipartFile> failureExampleImages,
                                                                     @PathVariable("challengeId") Long challengeId) throws IOException {

        for(MultipartFile multipartFile : successExampleImages){
            log.info("contentType ::::::::: " + multipartFile.getContentType());
        }
        imageService.uploadCertifyExampleImages(successExampleImages, challengeId, memberId, "SUCCESS");
        imageService.uploadCertifyExampleImages(failureExampleImages, challengeId, memberId, "FAILURE");
        return ResponseEntity.ok(CommonResponse.from("인증 예시 이미지가 업로드 되었습니다."));
    }

    @Tag(name = "challenge", description = "챌린지 API")
    @Operation(summary = "내가 참가한 챌린지 리스트 조회 api")
    @GetMapping("/participate")
    public ResponseEntity<List<MyParticipateChallengeResponseDto>> getMyParticipateChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId) {

        List<Participate> participates = challengeService.getMyParticipateChallenge(memberId);

        List<MyParticipateChallengeResponseDto> result = new ArrayList<>();
        for(Participate participate : participates){
            if(participate.getChallenge().getJudgeStatus().equals(JudgeStatus.COMPLETE)){
                result.add(MyParticipateChallengeResponseDto.from(participate.getChallenge(), String.valueOf(participate.getParticipateStatus())));
            }
        }
        return ResponseEntity.ok(result);
    }

    @Tag(name = "home", description = "홈(둘러보기) API")
    @Operation(summary = "홈 - 인기 챌린지, 공식 챌린지 조회 api")
    @GetMapping("/home")
    public ResponseEntity<ChallengeHomeResponseDto> getChallengeHome(HttpServletRequest httpServletRequest) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        // 인기 챌린지
        List<ChallengeResponseVo> popularChallengeVos = challengeService.getPopularChallenges();
        List<ChallengeResponseDto> popularChallengeResponseDtos = makeChallengeResponseDto(tokenString, popularChallengeVos);

        // 공식 챌린지
        List<ChallengeResponseVo> officialChallengeVos = challengeService.getOfficialChallenges();
        List<ChallengeResponseDto> officialChallengeResponseDtos = makeChallengeResponseDto(tokenString, officialChallengeVos);

        return ResponseEntity.ok(ChallengeHomeResponseDto.from(popularChallengeResponseDtos, officialChallengeResponseDtos));
    }

    @Tag(name = "home", description = "홈(둘러보기) API")
    @Operation(summary = "홈 - 카테고리 별 챌린지 조회 api", description = "- {category} - T: 재테크, M: 돈관리, L: 금융학습, C: 소비절약")
    @GetMapping("/home/{category}")
    public ResponseEntity<List<ChallengeResponseDto>> getChallengeHomeCategory(HttpServletRequest httpServletRequest,
                                                                               @PathVariable("category") String category) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        List<ChallengeResponseVo> challengeVos = challengeService.getCategoryChallenges(category);
        List<ChallengeResponseDto> challengeResponseDtos = makeChallengeResponseDto(tokenString, challengeVos);
        return ResponseEntity.ok(challengeResponseDtos);
    }

    @Tag(name = "home", description = "홈(둘러보기) API")
    @Operation(summary = "인기 챌린지 리스트 조회 api", description = "- {filter} - latest: 최신순, participate: 참여순, like: 좋아요순")
    @GetMapping("/popular/{filter}")
    public ResponseEntity<List<ChallengeResponseDto>> getPopularChallengeList(HttpServletRequest httpServletRequest,
                                                                               @PathVariable("filter") String filter) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        List<ChallengeResponseVo> challengeVos = challengeService.getPopularChallengeList(filter);
        List<ChallengeResponseDto> challengeResponseDtos = makeChallengeResponseDto(tokenString, challengeVos);
        return ResponseEntity.ok(challengeResponseDtos);
    }

    @Tag(name = "home", description = "홈(둘러보기) API")
    @Operation(summary = "공식 챌린지 리스트 조회 api", description = "- {filter} - latest: 최신순, participate: 참여순, like: 좋아요순")
    @GetMapping("/official/{filter}")
    public ResponseEntity<List<ChallengeResponseDto>> getOfficialChallengeList(HttpServletRequest httpServletRequest,
                                                                              @PathVariable("filter") String filter) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        List<ChallengeResponseVo> challengeVos = challengeService.getOfficialChallengeList(filter);
        List<ChallengeResponseDto> challengeResponseDtos = makeChallengeResponseDto(tokenString, challengeVos);
        return ResponseEntity.ok(challengeResponseDtos);
    }

    @Tag(name = "challenge", description = "챌린지 API")
    @Operation(summary = "챌린지 상세 조회 api - 챌린지")
    @GetMapping("/detail/info/{challengeId}")
    public ResponseEntity<ChallengeDetailResponseDto> getChallengeDetail(HttpServletRequest httpServletRequest,
                                                                               @PathVariable("challengeId") Long challengeId) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        Challenge challenge = challengeService.getChallenge(challengeId);

        // 챌린지 정보
        ChallengeInfoResponseDto challengeInfo = ChallengeInfoResponseDto.from(challenge);

        // 챌린지 규칙
        ChallengeRuleResponseDto challengeRule = ChallengeRuleResponseDto.from(challenge);

        // 인증 미션
        CertifyMissionResponseDto certifyMission = CertifyMissionResponseDto.from(
                challenge,
                getImageUrlByType("SUCCESS", challenge),
                getImageUrlByType("FAILURE", challenge)
        );

        // 챌린지 호스트
        Optional<Member> hostMember = challenge.getParticipates().stream()
                .filter(participate -> participate.getIsCreator().equals(true))
                .map(Participate::getMember)
                .findFirst();
        SimpleMemberResponseDto simpleMember = hostMember.map(SimpleMemberResponseDto::from).orElse(null);

        String myChallengeStatus = "";
        ChallengeConditionResponseDto challengeCondition = null;
        ChallengeDetailResponseDto result = null;
        if (tokenString == null || tokenString.isEmpty()) {     // 로그인 x
            // 챌린지 상태값 조회
            myChallengeStatus = getChallengeStatus(challenge);

            // 챌린지 현황
            challengeCondition = ChallengeConditionResponseDto.from(challenge);

            result = ChallengeDetailResponseDto.from(challenge, myChallengeStatus, challengeCondition, challengeInfo, challengeRule, certifyMission, simpleMember);

        } else{     // 로그인 o
            // 참여하는 챌린지인지 확인
            Member member = memberService.find(TokenProvider.getMemberIdKakao(tokenString));
            boolean isParticipate = false;
            for(Participate participate : member.getParticipates()){
                if(participate.getChallenge().equals(challenge)){
                    isParticipate = true;
                }
            }

            if(isParticipate){
                myChallengeStatus = getMyChallengeStatus(challenge, member);       // 챌린지 상태값 조회
                challengeCondition = ChallengeConditionResponseDto.from(challenge, member);       // 챌린지 현황
            }else{
                myChallengeStatus = getChallengeStatus(challenge);
                challengeCondition = ChallengeConditionResponseDto.from(challenge);
            }

            result = ChallengeDetailResponseDto.from(challenge, myChallengeStatus, challenge.isLike(member), challengeCondition, challengeInfo, challengeRule, certifyMission, simpleMember);
        }

        return ResponseEntity.ok(result);
    }

    @Tag(name = "challenge", description = "챌린지 API")
    @Operation(summary = "챌린지 상세 조회 api - 피드",  description = "- {filter} - latest: 최신순, like: 좋아요순")
    @GetMapping("/detail/feed/{challengeId}/{filter}")
    public ResponseEntity<ChallengeDetailFeedResponseDto> getChallengeDetailFeed(HttpServletRequest httpServletRequest,
                                                                                 @PathVariable("challengeId") Long challengeId,
                                                                                 @PathVariable("filter") String filter) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        Challenge challenge = challengeService.getChallenge(challengeId);
        List<Certify> certifies = certifyService.getChallengeCertifyList(challenge, filter);

        List<String> certifyImageUrlList = certifies.stream().map(certify -> {
            return certify.getCertifyImageList().get(0).getImageUrl();
        }).toList();

        List<SimpleCertifyResponseDto> certifyResponseDtos = new ArrayList<>();
        if (tokenString == null || tokenString.isEmpty()) {     // 로그인 x

            for(Certify certify : certifies){
                certifyResponseDtos.add(SimpleCertifyResponseDto.from(certify));
            }

        } else{
            // 로그인 o
            Member member = memberService.find(TokenProvider.getMemberIdKakao(tokenString));
            for(Certify certify : certifies){

                if(!checkIsBlocked(member, certify)) {
                    certifyResponseDtos.add(SimpleCertifyResponseDto.from(certify, findIsLike(challenge, member.getMemberId())));
                }
            }
        }

        return ResponseEntity.ok(ChallengeDetailFeedResponseDto.from(challenge, certifyImageUrlList, certifyResponseDtos));
    }

    private Boolean checkIsBlocked(Member member, Certify certify){

        for (Block block : member.getBlocks()){
            if (block.getBlockType().equals(BlockType.FEED) && block.getCertify().equals(certify)){
                return true;
            }
            else if (block.getBlockType().equals(BlockType.MEMBER) && block.getMember().equals(member)){
                return true;
            }
        }
        return false;
    }

    // 챌린지 상세 조회 - 버튼 리턴값 조회 (참여 o)
    private String getMyChallengeStatus(Challenge challenge, Member member) {

        long day = 0;
        if (challenge.getChallengeStatus().equals(ChallengeStatus.END)){
            return "챌린지 종료";
        }
        else if (challenge.isParticipants(member)
                    && (challenge.getChallengeStatus().equals(ChallengeStatus.POSSIBLE) || challenge.getChallengeStatus().equals(ChallengeStatus.IMPOSSIBLE))
                    && (!challenge.checkCertifyToday(member))){
            return "오늘 참여 인증하기";
        }
        else if (challenge.isParticipants(member)
                && (challenge.getChallengeStatus().equals(ChallengeStatus.POSSIBLE) || challenge.getChallengeStatus().equals(ChallengeStatus.IMPOSSIBLE))
                && (challenge.checkCertifyToday(member))) {
            return "오늘 인증완료";
        }
        else if(challenge.isParticipants(member)
                && challenge.getChallengeStatus().equals(ChallengeStatus.RECRUITING)){
            return "챌린지 기다리는 중";
        }
        else if((!challenge.isParticipants(member))
                && challenge.getChallengeStatus().equals(ChallengeStatus.POSSIBLE)){
            return "이 챌린지 참여하기";
        }
        else if((!challenge.isParticipants(member))
                && challenge.getChallengeStatus().equals(ChallengeStatus.IMPOSSIBLE)
                && challenge.getRecruitStartDate().isBefore(LocalDate.now())){
            return "챌린지 진행중";
        }
        else if((!challenge.isParticipants(member))
                && challenge.getChallengeStatus().equals(ChallengeStatus.IMPOSSIBLE)
                && challenge.getRecruitStartDate().isAfter(LocalDate.now())){
            return "챌린지 모집 마감";
        }
        else if((!challenge.isParticipants(member))
                && challenge.getChallengeStatus().equals(ChallengeStatus.POSSIBLE)
                && challenge.getRecruitStartDate().isAfter(LocalDate.now())){

            if(challenge.getRecruitStartDate().isAfter(LocalDate.now())){
                day = ChronoUnit.DAYS.between((Temporal) challenge.getRecruitStartDate(), LocalDate.now());
            }
            return "챌린지 기다리기 D-" + day;
        }
        else if(challenge.getJudgeStatus().equals(JudgeStatus.COMPLETE)
                && challenge.getRecruitStartDate().isAfter(LocalDate.now())){
            return "챌린지 심사 완료";
        }
        else{
            return "예외 케이스 발생!! ! ! !";
        }

    }

    // 챌린지 상세 조회 - 버튼 리턴값 조회 (참여 x)
    private String getChallengeStatus(Challenge challenge) {

        long day = 0;
        if (challenge.getChallengeStatus().equals(ChallengeStatus.END)){
            return "챌린지 종료";
        }
        else if(challenge.getChallengeStatus().equals(ChallengeStatus.POSSIBLE) || challenge.getChallengeStatus().equals(ChallengeStatus.RECRUITING)){
            return "이 챌린지 참여하기";
        }
        else if(challenge.getChallengeStatus().equals(ChallengeStatus.IMPOSSIBLE)
                && challenge.getRecruitStartDate().isBefore(LocalDate.now())){
            return "챌린지 진행중";
        }
        else if(challenge.getChallengeStatus().equals(ChallengeStatus.POSSIBLE)
                && challenge.getRecruitStartDate().isAfter(LocalDate.now())){

            if(challenge.getRecruitStartDate().isAfter(LocalDate.now())){
                day = ChronoUnit.DAYS.between((Temporal) challenge.getRecruitStartDate(), LocalDate.now());
            }
            return "챌린지 기다리기 D-" + day;
        }
        else{
            return "예외 케이스 발생!! ! ! !";
        }

    }

    private String getImageUrlByType(String type, Challenge challenge) {
        return imageService.getCertifyImage(challenge).stream()
                .filter(image -> image.getExampleType().equals(type))
                .map(CertifyExampleImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }

    @Tag(name = "home", description = "홈(둘러보기) API")
    @Operation(summary = "전체 챌린지 리스트 조회 api", description = "- {filter} - latest: 최신순, participate: 참여순, like: 좋아요순")
    @GetMapping("/list/{filter}")
    public ResponseEntity<List<ChallengeResponseDto>> getChallengeList(HttpServletRequest httpServletRequest,
                                                                       @PathVariable("filter") String filter) {

        final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        List<ChallengeResponseVo> challengeVos = challengeService.getChallengeList(filter);
        List<ChallengeResponseDto> challengeResponseDtos = makeChallengeResponseDto(tokenString, challengeVos);
        return ResponseEntity.ok(challengeResponseDtos);
    }


    private List<ChallengeResponseDto> makeChallengeResponseDto(String tokenString, List<ChallengeResponseVo> challengeVos){

        List<ChallengeResponseDto> challengeResponseDtos = new ArrayList<>();
        if (tokenString == null || tokenString.isEmpty()) {
            // 로그인 x
            challengeResponseDtos = challengeVos.stream().map(vo -> {
                return ChallengeResponseDto.from(vo.getChallenge(), vo.getParticipateNum(),
                        ChronoUnit.DAYS.between(LocalDate.now(), vo.getChallenge().getChallengeStartDate()));
            }).toList();
        } else{
            // 로그인 o
            challengeResponseDtos = challengeVos.stream().map(vo -> {
                return ChallengeResponseDto.from(vo.getChallenge(), vo.getParticipateNum(),
                        findIsLike(vo.getChallenge(), TokenProvider.getMemberIdKakao(tokenString)), ChronoUnit.DAYS.between(LocalDate.now(), vo.getChallenge().getChallengeStartDate()));
            }).toList();
        }

        return challengeResponseDtos;
    }

    private List<ChallengeResponseDto> makeChallengeResponseDtoV2(Long memberId, List<ChallengeResponseVo> challengeVos){

        List<ChallengeResponseDto> challengeResponseDtos = new ArrayList<>();
        challengeResponseDtos = challengeVos.stream().map(vo -> {
            return ChallengeResponseDto.from(vo.getChallenge(), vo.getParticipateNum(),
                    findIsLike(vo.getChallenge(), memberId), ChronoUnit.DAYS.between(LocalDate.now(), vo.getChallenge().getChallengeStartDate()));
        }).toList();

        return challengeResponseDtos;
    }

    private Boolean findIsLike(Challenge challenge, Long memberId){

        for(ChallengeLike challengeLike : challenge.getChallengeLikes()){
            if (challengeLike.getMember().getMemberId().equals(memberId)){
                return true;
            }
        }
        return false;
    }

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 참여하기 api")
    @PostMapping(value="/participate")
    public ResponseEntity<CommonResponse> createChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                                      @RequestBody @Valid ChallengeParticipateRequestDto req){

        participateService.create(req.getChallengeId(), memberId);
        return ResponseEntity.ok(CommonResponse.from("챌린지 참여가 완료되었습니다"));
    }

    @Tag(name = "challenge")
    @Operation(summary = "내 챌린지 조회 api")
    @GetMapping(value="/my")
    public ResponseEntity<MyChallengeResponseDto> getMyChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId){

        // 진행중인 챌린지
        List<Challenge> ongoingChallenges = challengeService.getMyOngoingChallenge(memberId);
        List<OngoingChallengeResponseDto> ongoingChallengeResponseDtos = ongoingChallenges.stream().map(challenge -> {
            return OngoingChallengeResponseDto.from(challenge, getCertifyNum(challenge, memberId));
        }).toList();

        // 최근 종료된 챌린지
        List<Challenge> passedChallenges = challengeService.getMyPassedChallenge(memberId);
        List<PassedChallengeResponseDto> passedChallengeResponseDtos = passedChallenges.stream().map(challenge -> {
            return PassedChallengeResponseDto.from(challenge, String.valueOf(getParticipate(challenge, memberId).getParticipateStatus()), getSuccessNum(challenge));
        }).toList();

        // 심사 중인 챌린지
        List<Challenge> judgingChallenges = challengeService.getMyJudgingChallenge(memberId);
        List<JudgingChallengeResponseDto> judgingChallengeResponseDtos = new ArrayList<>(judgingChallenges.stream().map(JudgingChallengeResponseDto::from).toList());

        // 심사 상태 COMPLETE 정렬
        judgingChallengeResponseDtos.sort(Comparator.comparing(dto -> {
            if (dto.getJudgingStatus().equals("COMPLETE")) {
                return 0;
            } else {
                return 1;
            }
        }));

        return ResponseEntity.ok(MyChallengeResponseDto.from(OngoingChallengeListResponseDto.from(ongoingChallengeResponseDtos),
                PassedChallengeListResponseDto.from(passedChallengeResponseDtos), JudgingChallengeListResponseDto.from(judgingChallengeResponseDtos)));
    }

    @Tag(name = "challenge")
    @Operation(summary = "챌린지 삭제 api")
    @DeleteMapping(value="/{challengeId}")
    public ResponseEntity<CommonResponse> deleteChallenge(@Parameter(hidden = true) @RequestMemberId Long memberId,
                                                            @PathVariable("challengeId") Long challengeId) {

        certifyService.deleteChallenge(memberId, challengeId);
        return ResponseEntity.ok(CommonResponse.from("챌린지가 삭제되었습니다"));
    }

    @Tag(name = "myPage", description = "마이페이지 API")
    @Operation(summary = "관심 챌린지 리스트 조회 api")
    @GetMapping("/like")
    public ResponseEntity<List<ChallengeResponseDto>> getMyChallengeLike(@Parameter(hidden = true) @RequestMemberId Long memberId) {

        List<Challenge> challenges = challengeService.getMyChallengeLike(memberId);
        List<ChallengeResponseVo>challengeVos = challenges.stream().map(challenge -> {
            return new ChallengeResponseVo(challenge, challenge.getParticipates().size());
        }).collect(Collectors.toList());

        List<ChallengeResponseDto> challengeResponseDtos = makeChallengeResponseDtoV2(memberId, challengeVos);
        return ResponseEntity.ok(challengeResponseDtos);
    }

    private Integer getSuccessNum(Challenge challenge){

        int cnt = 0;
        for(Participate participate : challenge.getParticipates()){
            if(participate.getParticipateStatus().equals(ParticipateStatus.SUCCESS)){
                cnt += 1;
            }
        }
        return cnt;
    }

    private Participate getParticipate(Challenge challenge, Long memberId){

        for(Participate participate : challenge.getParticipates()){
            if(participate.getMember().getMemberId().equals(memberId)){
                return participate;
            }
        }
        return null;
    }

    private Integer getCertifyNum(Challenge challenge, Long memberId){

        for(Participate participate : challenge.getParticipates()){
            if(participate.getMember().getMemberId().equals(memberId)){
                return participate.getCertifies().size();
            }
        }
        return null;
    }


}
